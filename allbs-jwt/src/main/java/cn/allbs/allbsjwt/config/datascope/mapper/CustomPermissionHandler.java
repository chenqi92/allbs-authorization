package cn.allbs.allbsjwt.config.datascope.mapper;

import cn.allbs.allbsjwt.config.utils.SecurityUtils;
import cn.allbs.allbsjwt.config.vo.SysUser;
import cn.allbs.common.constant.StringPool;
import cn.allbs.mybatis.datascope.DataPmsHandler;
import cn.allbs.mybatis.datascope.ScopeField;
import cn.allbs.mybatis.utils.PluginUtils;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import org.springframework.stereotype.Component;

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

        SysUser sysUser = SecurityUtils.getUser();
        // 如果非权限用户则不往下执行，执行原sql
        if (sysUser == null) {
            return where;
        }
        // 在有权限的情况下查询用户所关联的企业列表
        Set<Long> permissionEntList = sysUser.getEntIdList();
//        if (permissionEntList.size() == 0) {
//            return where;
//        }
        TableInfo tableInfo = TableInfoHelper.getTableInfo(table.getName());
        String fieldName = Optional.ofNullable(tableInfo.getEntityType().getAnnotation(ScopeField.class)).map(ScopeField::value).orElse(DEFAULT_FILTER_FIELD);
        String finalFieldName = Optional.of(table).map(Table::getAlias).map(a -> a.getName() + StringPool.DOT + fieldName).orElse(fieldName);

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
}
