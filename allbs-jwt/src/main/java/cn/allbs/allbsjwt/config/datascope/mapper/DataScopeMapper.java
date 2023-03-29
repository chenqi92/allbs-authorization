package cn.allbs.allbsjwt.config.datascope.mapper;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 类 DataScopeMapper
 *
 * @author ChenQi
 * @date 2023/3/28
 */
@Mapper
@Component
public interface DataScopeMapper<T> extends BaseMapper<T> {

    /**
     * 根据 ID 查询
     *
     * @param id 主键ID
     */
    @Override
    @InterceptorIgnore
    T selectById(Serializable id);
}
