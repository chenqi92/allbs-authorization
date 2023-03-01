package cn.allbs.allbsjwt.config.datascope;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 类 DataScopeParamContentHolder
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/28 17:57
 */
public final class DataScopeParamContentHolder {

    private DataScopeParamContentHolder() {
    }

    private static final ThreadLocal<DataScopeParam> THREAD_PMS_HOLDER = new TransmittableThreadLocal<>();

    /**
     * 设置当前header中的权限
     *
     * @param dataScopeParam 需要过滤的权限
     */
    public static void set(DataScopeParam dataScopeParam) {
        THREAD_PMS_HOLDER.set(dataScopeParam);
    }

    /**
     * 获取header中的权限
     *
     * @return 权限
     */
    public static DataScopeParam get() {
        return THREAD_PMS_HOLDER.get();
    }

    public static void clear() {
        THREAD_PMS_HOLDER.remove();
    }
}
