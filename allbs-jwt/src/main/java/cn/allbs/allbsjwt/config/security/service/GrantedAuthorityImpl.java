package cn.allbs.allbsjwt.config.security.service;

import org.springframework.security.core.GrantedAuthority;

/**
 * 类 GrantedAuthorityImpl
 * </p>
 * 权限集合 储存权限和角色
 *
 * @author ChenQi
 * @since 2023/2/1 16:43
 */
public class GrantedAuthorityImpl implements GrantedAuthority {

    private static final long serialVersionUID = 1L;

    private String authority;

    public GrantedAuthorityImpl(String authority) {
        this.authority = authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
