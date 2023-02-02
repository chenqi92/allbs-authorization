package cn.allbs.allbsjwt.service.sys.impl;

import cn.allbs.allbsjwt.config.vo.MenuVO;
import cn.allbs.allbsjwt.dao.sys.SysMenuDao;
import cn.allbs.allbsjwt.entity.sys.SysMenuEntity;
import cn.allbs.allbsjwt.service.sys.SysMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 菜单权限表(sys_menu)表服务实现类
 *
 * @author chenqi
 * @since 2023-02-01 15:38:57
 */
@Service("sysMenuService")
@AllArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {

    private final SysMenuDao sysMenuDao;

    @Override
    public List<MenuVO> findMenuByRoleId(Long roleId) {
        // TODO 菜单根据角色添加缓存
        return baseMapper.listMenusByRoleId(roleId);
    }

}
