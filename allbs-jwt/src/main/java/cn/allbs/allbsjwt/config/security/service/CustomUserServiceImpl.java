package cn.allbs.allbsjwt.config.security.service;

import cn.allbs.allbsjwt.config.constant.SecurityConstant;
import cn.allbs.allbsjwt.config.dto.UserInfo;
import cn.allbs.allbsjwt.config.vo.SysUser;
import cn.allbs.allbsjwt.entity.sys.SysUserEntity;
import cn.allbs.allbsjwt.service.sys.SysUserService;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 类 CustomUserServiceImpl
 * </p>
 * 自定义用户登录校验
 *
 * @author ChenQi
 * @since 2023/2/1 17:27
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {

    private final SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = sysUserService.findUserInfoByUserName(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("指定用户不存在!");
        }
        // 权限标识的集合
        Set<String> dbAuthsSet = new HashSet<>(Arrays.asList(userInfo.getPermissions()));
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
        SysUserEntity user = userInfo.getSysUser();
        // 判断用户是否为正常使用的状态
        boolean enabled = StrUtil.equals(user.getLockFlag(), SecurityConstant.STATUS_NORMAL);
        // @formatter:off
        return new SysUser(
                // 用户id
                user.getUserId(),
                // 用户手机号
                user.getPhone(),
                // 用户头像
                user.getAvatar(),
                // 用户名
                user.getUsername(),
                // 密码
                user.getPassword(),
                // 用户账号是否为正常使用的状态
                enabled,
                true,
                true,
                // 判断用户是否为锁定状态
                !SecurityConstant.STATUS_LOCK.equals(user.getLockFlag()),
                // 权限列表
                authorities,
                Optional.ofNullable(userInfo.getEntIds()).orElse(new HashSet<>())
        );
        // @formatter:on
    }
}
