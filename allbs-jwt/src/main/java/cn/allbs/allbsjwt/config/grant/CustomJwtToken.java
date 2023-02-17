package cn.allbs.allbsjwt.config.grant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.allbs.allbsjwt.config.constant.SecurityConstant.BEARER_TYPE;

/**
 * 类 CustomJwtToken
 * </p>
 * 自定义登录后返回的token格式及内容
 *
 * @author ChenQi
 * @since 2023/2/3 14:45
 */
public class CustomJwtToken implements Serializable {

    private static final long serialVersionUID = 2149134569530465633L;

    @JsonIgnore
    private String value;

    /**
     * token
     */
    private String token;

    private String tokenType = BEARER_TYPE.toLowerCase();

    /**
     * 权限集合
     */
    private Set<String> permissions;

    public CustomJwtToken(String value) {
        this.value = value;
    }

    @SuppressWarnings("unused")
    private CustomJwtToken() {
        this((String) null);
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * The token value.
     *
     * @return The token value.
     */
    public String getValue() {
        return value;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<? extends GrantedAuthority> authorities) {
        this.permissions = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
