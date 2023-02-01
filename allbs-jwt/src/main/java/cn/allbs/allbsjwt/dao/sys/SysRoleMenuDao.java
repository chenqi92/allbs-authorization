package cn.allbs.allbsjwt.dao.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.allbs.allbsjwt.entity.sys.SysRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色菜单表(sys_role_menu)表数据库访问层
 *
 * @author chenqi
 * @since 2023-02-01 15:38:58
 */
@Mapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenuEntity> {

}
