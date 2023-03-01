package cn.allbs.allbsjwt.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * 类 DataScope
 * </p>
 * 需要进行数据过滤的类或者方法
 *
 * @author ChenQi
 * @since 2023/2/28 17:39
 */
@Target({METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 当进行过滤时主表中代表企业id的字段
     */
    String unitField() default "ent_id";

    /**
     * 是否进行数据过滤
     */
    boolean filterData() default true;

    /**
     * 忽略的表名
     *
     * @return 不进行数据过滤的表名的集合
     */
    String[] ignoreTables() default {"sys_file"};
}
