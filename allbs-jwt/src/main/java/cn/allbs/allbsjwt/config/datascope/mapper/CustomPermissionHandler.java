package cn.allbs.allbsjwt.config.datascope.mapper;

import cn.allbs.allbsjwt.config.utils.SecurityUtils;
import cn.allbs.allbsjwt.config.vo.SysUser;
import cn.allbs.common.constant.StringPool;
import cn.allbs.mybatis.datascope.DataPmsHandler;
import cn.allbs.mybatis.datascope.ScopeField;
import cn.allbs.mybatis.execption.UserOverreachException;
import cn.allbs.mybatis.utils.PluginUtils;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 类 CustomPermissionHandler
 *
 * @author ChenQi
 * @date 2023/3/28
 */
@Slf4j
@Component
public class CustomPermissionHandler implements DataPmsHandler {

    private final static String DEFAULT_FILTER_FIELD = "ent_id";

    /**
     * 获取数据权限 SQL 片段
     *
     * @param where             待执行 SQL Where 条件表达式
     * @param mappedStatementId Mybatis MappedStatement Id 根据该参数可以判断具体执行方法
     * @return JSqlParser 条件表达式
     */
    @Override
    public Expression getSqlSegment(final Table table, Expression where, String mappedStatementId) {

        // 在有权限的情况下查询用户所关联的企业列表
        SysUser sysUser = SecurityUtils.getUser();
        // 如果非权限用户则不往下执行，执行原sql
        if (sysUser == null) {
            return where;
        }
        Set<Long> permissionEntList = sysUser.getEntIdList();
//        if (permissionEntList.size() == 0) {
//            return where;
//        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(table.getName());
        String fieldName = tableInfo.getFieldList().stream()
                .filter(a -> a.getField().getAnnotation(ScopeField.class) != null)
                .map(a -> a.getField().getAnnotation(ScopeField.class).value())
                .findFirst()
                .orElse(DEFAULT_FILTER_FIELD);
        Alias fromItemAlias = table.getAlias();
        String finalFieldName = Optional.ofNullable(fromItemAlias).map(a -> a.getName() + StringPool.DOT + fieldName).orElse(fieldName);

        if (permissionEntList.size() > 1) {
            // 把集合转变为 JSQLParser需要的元素列表
            InExpression inExpression = new InExpression(new Column(finalFieldName), PluginUtils.getItemList(permissionEntList));

            // 组装sql
            return where == null ? inExpression : new AndExpression(where, inExpression);
        }
        // 设置where
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column(finalFieldName));
        equalsTo.setRightExpression(new LongValue(permissionEntList.stream().findFirst().orElse(0L)));
        return where == null ? equalsTo : new AndExpression(where, equalsTo);
    }

    @Override
    public void updateParameter(Update updateStmt, MappedStatement mappedStatement, BoundSql boundSql) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(updateStmt.getTable().getName());
        parameterHandler(tableInfo.getFieldList(), boundSql);
    }

    @Override
    public void insertParameter(Insert insertStmt, BoundSql boundSql) {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(insertStmt.getTable().getName());
        parameterHandler(tableInfo.getFieldList(), boundSql);
    }

    private void parameterHandler(List<TableFieldInfo> fieldList, BoundSql boundSql) {
        // 过滤数据
        SysUser sysUser = SecurityUtils.getUser();
        // 如果当前用户是超级管理员，不处理
        if (sysUser.getId() == 1L) {
            return;
        }
        // 获取当前用户所具备的ent_id
        Set<Long> permissionEntList = sysUser.getEntIdList();

        // 获取当前表中需要权限过滤的字段名称
        String fieldName = fieldList.stream()
                .filter(a -> a.getField().getAnnotation(ScopeField.class) != null)
                .map(a -> a.getField().getAnnotation(ScopeField.class).value())
                .findFirst()
                .orElse(DEFAULT_FILTER_FIELD);

        MetaObject metaObject = SystemMetaObject.forObject(boundSql.getParameterObject());

        for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
            String propertyName = parameterMapping.getProperty();
            if (propertyName.startsWith("ew.paramNameValuePairs")) {
                continue;
            }
            String[] arr = propertyName.split("\\.");
            String propertyNameTrim = arr[arr.length - 1].replace("_", "").toUpperCase();
            if (fieldName.replaceAll("[._\\-$]", "").toUpperCase().equals(propertyNameTrim)) {
                if (!Optional.ofNullable(metaObject.getValue(propertyName)).isPresent()) {
                    return;
                }
                long currentEntId = Long.parseLong(metaObject.getValue(propertyName).toString());
                // 判断是否在权限范围内
                if (permissionEntList.contains(currentEntId)) {
                    metaObject.setValue(propertyName, currentEntId);
                } else {
                    // 可以直接抛出异常 or 使用当前用户的ent_id 替换插入值 or 直接忽略当前插入sql但不抛出异常
                    throw new UserOverreachException();
                }
            }

        }
    }
}
