package cn.allbs.allbsjwt.service.sys;

import cn.allbs.allbsjwt.config.vo.MenuVO;
import cn.allbs.allbsjwt.entity.sys.SysMenuEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 菜单权限表(sys_menu)表服务接口
 *
 * @author chenqi
 * @since 2023-02-01 15:38:56
 */
public interface SysMenuService extends IService<SysMenuEntity> {

    /**
     * 通过角色编号查询URL 权限
     *
     * @param roleId 角色ID
     * @return 菜单列表
     */
    List<MenuVO> findMenuByRoleId(Long roleId);

    /**
     * 级联删除菜单
     *
     * @param id 菜单ID
     * @return 成功、失败
     */
    Boolean removeMenuById(Long id);

    /**
     * 更新菜单信息
     *
     * @param sysMenu 菜单信息
     * @return 成功、失败
     */
    Boolean updateMenuById(SysMenuEntity sysMenu);

}
