package cn.allbs.allbsjwt.service.sys.impl;

import cn.allbs.allbsjwt.config.vo.MenuVO;
import cn.allbs.allbsjwt.dao.sys.SysMenuDao;
import cn.allbs.allbsjwt.dao.sys.SysRoleMenuDao;
import cn.allbs.allbsjwt.entity.sys.SysMenuEntity;
import cn.allbs.allbsjwt.entity.sys.SysRoleMenuEntity;
import cn.allbs.allbsjwt.service.sys.SysMenuService;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static cn.allbs.allbsjwt.config.constant.CacheConstant.MENU_DETAILS;

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

    private final SysRoleMenuDao sysRoleMenuDao;

    @Override
    @Cacheable(value = MENU_DETAILS, key = "#roleId  + '_menu'", unless = "#result.isEmpty()")
    public List<MenuVO> findMenuByRoleId(Long roleId) {
        return baseMapper.listMenusByRoleId(roleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = MENU_DETAILS, allEntries = true)
    public Boolean removeMenuById(Long id) {
        // 查询父节点为当前节点的节点
        List<SysMenuEntity> menuList = this.list(Wrappers.<SysMenuEntity>query()
                .lambda().eq(SysMenuEntity::getParentId, id));
        if (CollUtil.isNotEmpty(menuList)) {
            return false;
        }

        sysRoleMenuDao.delete(Wrappers.<SysRoleMenuEntity>query()
                .lambda().eq(SysRoleMenuEntity::getMenuId, id));
        // 删除当前菜单及其子菜单
        return this.removeById(id);
    }

    @Override
    @CacheEvict(value = MENU_DETAILS, allEntries = true)
    public Boolean updateMenuById(SysMenuEntity sysMenu) {
        return this.updateById(sysMenu);
    }

}
