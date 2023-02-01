package cn.allbs.allbsjwt.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.allbs.allbsjwt.entity.sys.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统角色表(sys_role)表数据库访问层
 *
 * @author chenqi
 * @since 2023-02-01 15:38:57
 */
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

}
