package cn.allbs.allbsjwt.service.sys.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.allbs.allbsjwt.dao.sys.SysUserDao;
import cn.allbs.allbsjwt.entity.sys.SysUserEntity;
import cn.allbs.allbsjwt.service.sys.SysUserService;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

/**
 * 用户表(sys_user)表服务实现类
 *
 * @author chenqi
 * @since 2023-02-01 15:38:59
 */
@Service("sysUserService")
@AllArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    private final SysUserDao sysUserDao;

}
