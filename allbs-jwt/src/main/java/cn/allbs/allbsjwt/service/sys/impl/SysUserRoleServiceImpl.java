package cn.allbs.allbsjwt.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.allbs.allbsjwt.dao.sys.SysUserRoleDao;
import cn.allbs.allbsjwt.entity.sys.SysUserRoleEntity;
import cn.allbs.allbsjwt.service.sys.SysUserRoleService;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

/**
 * 用户角色表(sys_user_role)表服务实现类
 *
 * @author chenqi
 * @since 2023-02-01 15:38:59
 */
@Service("sysUserRoleService")
@AllArgsConstructor
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {

    private final SysUserRoleDao sysUserRoleDao;

}
