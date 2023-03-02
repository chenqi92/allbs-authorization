package cn.allbs.allbsjwt.config.security.service;

import cn.allbs.allbsjwt.config.utils.SecurityUtils;
import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 类 PermissionService
 * </p>
 * 用户权限判断
 *
 * @author ChenQi
 * @since 2023/3/2 15:42
 */
@Slf4j
@Component("pms")
public class PermissionService {
    /**
     * 判断接口是否有任意xxx，xxx权限
     *
     * @param permissions 权限
     * @return {boolean}
     */
    public boolean hasPermission(String... permissions) {
        if (ArrayUtil.isEmpty(permissions)) {
            return false;
        }
        Authentication authentication = SecurityUtils.getAuthentication();
        if (authentication == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(permissions, x));
    }
}
