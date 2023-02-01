package cn.allbs.allbsjwt.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.allbs.allbsjwt.dao.sys.SysRoleMenuDao;
import cn.allbs.allbsjwt.entity.sys.SysRoleMenuEntity;
import cn.allbs.allbsjwt.service.sys.SysRoleMenuService;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

/**
 * 角色菜单表(sys_role_menu)表服务实现类
 *
 * @author chenqi
 * @since 2023-02-01 15:38:58
 */
@Service("sysRoleMenuService")
@AllArgsConstructor
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> implements SysRoleMenuService {

    private final SysRoleMenuDao sysRoleMenuDao;

}
