package cn.allbs.allbsjwt.config.datascope.mapper;

import cn.allbs.allbsjwt.config.utils.SecurityUtils;
import cn.allbs.allbsjwt.config.vo.SysUser;
import cn.allbs.common.constant.StringPool;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;

import java.util.LinkedList;
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
public class CustomPermissionHandler implements AllbsDataPermissionHandler {

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
            InExpression inExpression = new InExpression(new Column(finalFieldName), getItemList(permissionEntList));

            // 组装sql
            return where == null ? inExpression : new AndExpression(where, inExpression);
        }
        // 设置where
        EqualsTo equalsTo = new EqualsTo();
        equalsTo.setLeftExpression(new Column(finalFieldName));
        equalsTo.setRightExpression(new LongValue(permissionEntList.stream().findFirst().orElse(0L)));
        return where == null ? equalsTo : new AndExpression(where, equalsTo);
    }

    private ItemsList getItemList(Set<Long> permissionEntList) {
        List<Expression> list = new LinkedList<>();
        for (Long aLong : permissionEntList) {
            try {
                Expression expression = CCJSqlParserUtil.parseCondExpression(StrUtil.join(",", aLong));
                list.add(expression);
            } catch (JSQLParserException e) {
                log.error("筛选数据转换为表达式失败!" + e.getLocalizedMessage());
            }
        }
        return new ExpressionList(list);
    }
}
