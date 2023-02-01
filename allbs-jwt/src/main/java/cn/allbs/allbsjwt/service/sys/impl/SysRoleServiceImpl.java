package cn.allbs.allbsjwt.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.allbs.allbsjwt.dao.sys.SysRoleDao;
import cn.allbs.allbsjwt.entity.sys.SysRoleEntity;
import cn.allbs.allbsjwt.service.sys.SysRoleService;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

/**
 * 系统角色表(sys_role)表服务实现类
 *
 * @author chenqi
 * @since 2023-02-01 15:38:57
 */
@Service("sysRoleService")
@AllArgsConstructor
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {

    private final SysRoleDao sysRoleDao;

}
