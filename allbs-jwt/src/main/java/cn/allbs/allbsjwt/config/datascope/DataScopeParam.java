package cn.allbs.allbsjwt.config.datascope;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 类 DataScopeParam
 * </p>
 * 用户保存待过滤的参数信息
 *
 * @author ChenQi
 * @since 2023/2/28 17:34
 */
@Data
@AllArgsConstructor
public class DataScopeParam {

    /**
     * 企业筛选字段名称（比如某个表中并未使用其他表通用的字段ent_id进行区分企业）
     */
    private String unitField;

    /**
     * 企业数据范围
     */
    private Set<Long> entIdList;

    /**
     * 是否进行拦截
     */
    private boolean filterField;

    /**
     * 忽略不过滤的表名
     */
    private List<String> ignoreTables;
}
