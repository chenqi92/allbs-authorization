package cn.allbs.allbsjwt.dao.sys;

import cn.allbs.allbsjwt.config.vo.MenuVO;
import cn.allbs.allbsjwt.entity.sys.SysMenuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 菜单权限表(sys_menu)表数据库访问层
 *
 * @author chenqi
 * @since 2023-02-01 15:38:56
 */
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {

    /**
     * 通过角色编号查询菜单
     *
     * @param roleId 角色ID
     * @return
     */
    List<MenuVO> listMenusByRoleId(@Param("roleId") Long roleId);

}
