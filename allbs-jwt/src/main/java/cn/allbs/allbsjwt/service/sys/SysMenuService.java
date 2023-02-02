package cn.allbs.allbsjwt.service.sys;

import cn.allbs.allbsjwt.config.vo.MenuVO;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.allbs.allbsjwt.entity.sys.SysMenuEntity;

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

}
