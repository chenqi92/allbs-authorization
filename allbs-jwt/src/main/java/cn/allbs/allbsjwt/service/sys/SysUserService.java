package cn.allbs.allbsjwt.service.sys;

import cn.allbs.allbsjwt.config.dto.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.allbs.allbsjwt.entity.sys.SysUserEntity;

/**
 * 用户表(sys_user)表服务接口
 *
 * @author chenqi
 * @since 2023-02-01 15:38:58
 */
public interface SysUserService extends IService<SysUserEntity> {

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户
     * @return userInfo
     */
    UserInfo findUserInfoByUserName(String username);

}
