package cn.allbs.allbsjwt.config.datascope;

import cn.allbs.allbsjwt.config.annotation.DataScope;
import cn.hutool.core.convert.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.MethodClassKey;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.util.ClassUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 类 DataScopeAnnotationClassResolver
 * </p>
 * 权限解析器
 *
 * @author ChenQi
 * @since 2023/2/28 17:52
 */
@Slf4j
public class DataScopeAnnotationClassResolver {

    /**
     * 缓存方法对应的权限拦截
     */
    private final Map<Object, DataScopeParam> dsCache = new ConcurrentHashMap<>();

    public DataScopeAnnotationClassResolver() {
    }

    /**
     * 从缓存获取数据
     *
     * @param method       方法
     * @param targetObject 目标对象
     * @return ds
     */
    public DataScopeParam findKey(Method method, Object targetObject) {
        if (method.getDeclaringClass() == Object.class) {
            return null;
        }
        Object cacheKey = new MethodClassKey(method, targetObject.getClass());
        DataScopeParam dsp = this.dsCache.get(cacheKey);
        if (dsp == null) {
            dsp = computeDatasource(method, targetObject);
            this.dsCache.put(cacheKey, dsp);
        }
        return dsp;
    }

    /**
     * 查找注解的顺序
     * 1. 当前方法
     * 2. 桥接方法
     * 3. 当前类开始一直找到Object
     *
     * @param method       方法
     * @param targetObject 目标对象
     * @return ds
     */
    private DataScopeParam computeDatasource(Method method, Object targetObject) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        //1. 从当前方法接口中获取
        DataScopeParam dsAttr = findDataSourceAttribute(method);
        if (dsAttr != null) {
            return dsAttr;
        }
        Class<?> targetClass = targetObject.getClass();
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        // JDK代理时,  获取实现类的方法声明.  method: 接口的方法, specificMethod: 实现类方法
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);

        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        //2. 从桥接方法查找
        dsAttr = findDataSourceAttribute(specificMethod);
        if (dsAttr != null) {
            return dsAttr;
        }
        // 从当前方法声明的类查找
        dsAttr = findDataSourceAttribute(userClass);
        if (dsAttr != null && ClassUtils.isUserLevelMethod(method)) {
            return dsAttr;
        }
        //since 3.4.1 从接口查找，只取第一个找到的
        for (Class<?> interfaceClazz : ClassUtils.getAllInterfacesForClassAsSet(userClass)) {
            dsAttr = findDataSourceAttribute(interfaceClazz);
            if (dsAttr != null) {
                return dsAttr;
            }
        }
        // 如果存在桥接方法
        if (specificMethod != method) {
            // 从桥接方法查找
            dsAttr = findDataSourceAttribute(method);
            if (dsAttr != null) {
                return dsAttr;
            }
            // 从桥接方法声明的类查找
            dsAttr = findDataSourceAttribute(method.getDeclaringClass());
            if (dsAttr != null && ClassUtils.isUserLevelMethod(method)) {
                return dsAttr;
            }
        }
        return getDefaultDataSourceAttr(targetObject);
    }

    /**
     * 默认的获取
     *
     * @param targetObject 目标对象
     * @return DataScopeParam
     */
    private DataScopeParam getDefaultDataSourceAttr(Object targetObject) {
        Class<?> targetClass = targetObject.getClass();
        // 如果不是代理类, 从当前类开始, 不断的找父类的声明
        if (!Proxy.isProxyClass(targetClass)) {
            Class<?> currentClass = targetClass;
            while (currentClass != Object.class) {
                DataScopeParam datasourceAttr = findDataSourceAttribute(currentClass);
                if (datasourceAttr != null) {
                    return datasourceAttr;
                }
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 通过 AnnotatedElement 查找标记的注解, 映射为  DatasourceHolder
     *
     * @param ae AnnotatedElement
     * @return 数据源映射持有者
     */
    private DataScopeParam findDataSourceAttribute(AnnotatedElement ae) {
        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ae, DataScope.class);
        DataScopeParam dsp = null;
        if (attributes != null) {
            dsp = new DataScopeParam(attributes.getString("unitField"), new HashSet<>(), attributes.getBoolean("filterData"), Convert.toList(String.class, attributes.get("ignoreTables")));
        }
        return dsp;
    }
}
