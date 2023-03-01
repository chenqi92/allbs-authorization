package cn.allbs.allbsjwt.config.datascope;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

import java.util.Set;

/**
 * 类 CurrentEntIdSearchContextHolder
 * </p>
 *
 * @author ChenQi
 * @since 2023/3/1 10:43
 */
@UtilityClass
public class CurrentEntIdSearchContextHolder {

    private final ThreadLocal<Set<Long>> THREAD_LOCAL_ENT_LIST = new TransmittableThreadLocal<>();

    /**
     * 设置当前header中的企业列表
     *
     * @param entIdList 需要查询的企业列表
     */
    public void setEntIdList(Set<Long> entIdList) {
        THREAD_LOCAL_ENT_LIST.set(entIdList);
    }

    /**
     * 获取header中的企业列表
     *
     * @return 企业列表
     */
    public Set<Long> getEntIdList() {
        return THREAD_LOCAL_ENT_LIST.get();
    }

    public void clear() {
        THREAD_LOCAL_ENT_LIST.remove();
    }
}
