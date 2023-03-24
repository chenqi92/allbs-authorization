package cn.allbs.allbsjwt.config.datascope;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;

/**
 * 类 DataScopeAnnotationIntercept
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/28 17:57
 */
public class DataScopeAnnotationIntercept implements MethodInterceptor {

    private final DataScopeAnnotationClassResolver dataScopeAnnotationClassResolver;

    public DataScopeAnnotationIntercept() {
        dataScopeAnnotationClassResolver = new DataScopeAnnotationClassResolver();
    }

    @Nullable
    @Override
    public Object invoke(@NotNull MethodInvocation methodInvocation) throws Throwable {
        DataScopeParam paramKey = dataScopeAnnotationClassResolver.findKey(methodInvocation.getMethod(), methodInvocation.getThis());
        DataScopeParamContentHolder.set(paramKey);
        try {
            return methodInvocation.proceed();
        } finally {
            DataScopeParamContentHolder.clear();
        }
    }
}
