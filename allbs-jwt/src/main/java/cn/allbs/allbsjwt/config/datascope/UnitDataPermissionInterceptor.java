package cn.allbs.allbsjwt.config.datascope;

import cn.allbs.allbsjwt.config.exception.UnauthorizedException;
import cn.allbs.allbsjwt.config.utils.SecurityUtils;
import cn.allbs.allbsjwt.config.vo.SysUser;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 类 UnitDataPermissionInterceptor
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/28 18:09
 */
@Aspect
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Component
public class UnitDataPermissionInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 在有权限的情况下查询用户所关联的企业列表
        SysUser sysUser = SecurityUtils.getUser();
        // 如果非权限用户则不往下执行
        if (sysUser == null) {
            return invocation.proceed();
        }

        DataScopeParam dataScopeParam = DataScopeParamContentHolder.get();

        if (dataScopeParam != null) {
            dataScopeParam.setEntIdList(sysUser.getEntIdList());
        }

        // 获取header中的待过滤的企业列表
        Set<Long> entIdList = CurrentEntIdSearchContextHolder.getEntIdList();
        if (entIdList != null) {
            if (dataScopeParam == null) {
                dataScopeParam = new DataScopeParam("ent_id", entIdList, true, CollUtil.newArrayList("sys_file"));
            } else {
                // 查询交集
                Set<Long> permissionEntList = dataScopeParam.getEntIdList();
                dataScopeParam.setFilterField(true);
                dataScopeParam.setEntIdList(entIdList.stream().filter(permissionEntList::contains).collect(Collectors.toSet()));
            }
        }

        // 没有添加注解则不往下执行
        if (dataScopeParam == null) {
            return invocation.proceed();
        }

        // 注解配置不过滤数据则不往下执行
        if (!dataScopeParam.isFilterField()) {
            return invocation.proceed();
        }

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        // 先判断是不是SELECT操作 不是直接过滤
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (SqlCommandType.FLUSH.equals(mappedStatement.getSqlCommandType()) || SqlCommandType.UNKNOWN.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        // 执行的SQL语句
        String originalSql = boundSql.getSql();
        // SQL语句的参数
        Object parameterObject = boundSql.getParameterObject();
        // 拦截插入语句
        if (SqlCommandType.INSERT.equals(mappedStatement.getSqlCommandType())) {
            // 当为insert时将判断是否具备权限
            if (parameterObject != null) {
                Long entId = Convert.toLong(ReflectUtil.getFieldValue(parameterObject, StrUtil.toCamelCase(dataScopeParam.getUnitField())));
                // 判断entId是否在权限范围内
                if (entId != null && !dataScopeParam.getEntIdList().contains(entId)) {
                    throw new UnauthorizedException("entId不在权限范围内");
                }
            }
            return invocation.proceed();
        }
        // 拦截更新语句，业务包含逻辑删除所以此处用的update
        if (SqlCommandType.UPDATE.equals(mappedStatement.getSqlCommandType())) {
            // 修改updateSql
            String updateSql = handleUpdateSql(originalSql, dataScopeParam.getEntIdList(), dataScopeParam.getUnitField(), dataScopeParam.getIgnoreTables());
            log.warn("数据权限处理过后UPDATE的SQL: {}", updateSql);
            metaObject.setValue("delegate.boundSql.sql", updateSql);
            return invocation.proceed();
        }
        // 需要过滤的数据
        String finalSql = this.handleSql(originalSql, dataScopeParam.getEntIdList(), dataScopeParam.getUnitField(), dataScopeParam.getIgnoreTables());
        log.warn("数据权限处理过后SELECT的SQL: {}", finalSql);

        // 装载改写后的sql
        metaObject.setValue("delegate.boundSql.sql", finalSql);
        return invocation.proceed();
    }


    /**
     * 修改select语句sql
     *
     * @param originalSql 原始sql
     * @param entIdList   需要过滤的企业列表
     * @param fieldName   当前主表中字段名称
     * @return 修改后的语句
     * @throws JSQLParserException sql修改异常
     */
    private String handleSql(String originalSql, Set<Long> entIdList, String fieldName, List<String> ignores) throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(originalSql));
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            this.setWhere((PlainSelect) selectBody, entIdList, fieldName, ignores);
        } else if (selectBody instanceof SetOperationList) {
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selectBodyList = setOperationList.getSelects();
            selectBodyList.forEach(s -> this.setWhere((PlainSelect) s, entIdList, fieldName, ignores));
        }
        return select.toString();
    }

    /**
     * 修改update语句
     *
     * @param originalSql 元素sql
     * @param entIdList   允许查询的企业列表
     * @param fieldName   表中待过滤查询的列名
     * @param ignores     忽略的表名
     * @return
     * @throws JSQLParserException
     */
    private String handleUpdateSql(String originalSql, Set<Long> entIdList, String fieldName, List<String> ignores) throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Update update = (Update) parserManager.parse(new StringReader(originalSql));
        if (ignores.contains(update.getTable().getName())) {
            // 当前表名的处于不过滤列表则不进行二次封装处理
            return originalSql;
        }
        String dataPermissionSql;
        if (entIdList.size() == 1) {
            EqualsTo selfEqualsTo = new EqualsTo();
            selfEqualsTo.setLeftExpression(new Column(fieldName));
            selfEqualsTo.setRightExpression(new LongValue(entIdList.stream().findFirst().orElse(0L)));
            dataPermissionSql = selfEqualsTo.toString();
        } else {
            dataPermissionSql = fieldName + " in ( " + CollUtil.join(entIdList, StringPool.COMMA) + " )";
        }
        update.setWhere(new AndExpression(update.getWhere(), CCJSqlParserUtil.parseCondExpression(dataPermissionSql)));
        return update.toString();
    }

    /**
     * 设置 where 条件  --  使用CCJSqlParser将原SQL进行解析并改写
     *
     * @param plainSelect 查询对象
     */
    @SneakyThrows(Exception.class)
    protected void setWhere(PlainSelect plainSelect, Set<Long> entIdList, String fieldName, List<String> ignores) {
        Table fromItem = (Table) plainSelect.getFromItem();
        // 有别名用别名，无别名用表名，防止字段冲突报错
        Alias fromItemAlias = fromItem.getAlias();
        if (ignores.contains(fromItem.getName())) {
            // 当前表名的处于不过滤列表则不进行二次封装处理
            return;
        }
        String mainTableName = fromItemAlias == null ? fromItem.getName() : fromItemAlias.getName();
        // 构建子查询 -- 数据权限过滤SQL
        String dataPermissionSql;
        if (entIdList.size() == 1) {
            EqualsTo selfEqualsTo = new EqualsTo();
            selfEqualsTo.setLeftExpression(new Column(mainTableName + "." + fieldName));
            selfEqualsTo.setRightExpression(new LongValue(entIdList.stream().findFirst().orElse(0L)));
            dataPermissionSql = selfEqualsTo.toString();
        } else if (entIdList.size() < 1) {
            dataPermissionSql = mainTableName + "." + fieldName + " in ( " + StringPool.NULL + " )";
        } else {
            dataPermissionSql = mainTableName + "." + fieldName + " in ( " + CollUtil.join(entIdList, StringPool.COMMA) + " )";
        }

        if (plainSelect.getWhere() == null) {
            plainSelect.setWhere(CCJSqlParserUtil.parseCondExpression(dataPermissionSql));
        } else {
            plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), CCJSqlParserUtil.parseCondExpression(dataPermissionSql)));
        }
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {
        log.info(properties.toString());
    }
}
