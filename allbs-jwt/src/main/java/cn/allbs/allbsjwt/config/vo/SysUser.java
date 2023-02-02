package cn.allbs.allbsjwt.config.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * 类 SysUser
 * </p>
 * 自定义构建用户信息
 *
 * @author ChenQi
 * @since 2023/2/2 10:01
 */
public class SysUser extends User {

    /**
     * 用户ID
     */
    @Getter
    private final Long id;
    /**
     * 手机号
     */
    @Getter
    private final String phone;

    /**
     * 头像
     */
    @Getter
    private final String avatar;

    /**
     * 是否为网格员片区总管(0:不是,1:是)
     */
    @Getter
    @Setter
    private Integer isGridHeadUser;

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public SysUser(Long id, String phone, String avatar, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.phone = phone;
        this.avatar = avatar;
    }
}
