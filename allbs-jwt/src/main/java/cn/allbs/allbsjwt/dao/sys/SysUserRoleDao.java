package cn.allbs.allbsjwt.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.allbs.allbsjwt.entity.sys.SysUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户角色表(sys_user_role)表数据库访问层
 *
 * @author chenqi
 * @since 2023-02-01 15:38:59
 */
@Mapper
public interface SysUserRoleDao extends BaseMapper<SysUserRoleEntity> {

}
