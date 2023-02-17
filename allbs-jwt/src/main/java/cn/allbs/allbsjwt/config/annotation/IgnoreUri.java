package cn.allbs.allbsjwt.config.annotation;

import java.lang.annotation.*;

/**
 * 添加注解用于放行接口
 *
 * @author ChenQi
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IgnoreUri {

    boolean value() default true;
}
