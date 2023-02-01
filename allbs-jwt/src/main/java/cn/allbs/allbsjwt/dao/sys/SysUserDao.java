package cn.allbs.allbsjwt.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.allbs.allbsjwt.entity.sys.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表(sys_user)表数据库访问层
 *
 * @author chenqi
 * @since 2023-02-01 15:38:58
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {

}
