package cn.allbs.allbsjwt.config.filter;

import cn.allbs.allbsjwt.config.exception.AuthorizationException;
import cn.allbs.allbsjwt.config.exception.UnauthorizedException;
import cn.allbs.allbsjwt.config.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 类 SecurityAuthenticationFilter
 * </p>
 * token校验
 *
 * @author ChenQi
 * @since 2023/2/2 11:04
 */
@Slf4j
public class SecurityAuthenticationFilter extends BasicAuthenticationFilter {

    public SecurityAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = TokenUtil.getToken(request);
        if (!TokenUtil.validateToken(token)) {
            // token 验证不通过
            throw new UnauthorizedException("token失效!");
        }
        Authentication authentication = getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        String username = TokenUtil.getUsernameFromToken(token);
        if (StringUtils.hasText(username)) {
            // 查询当前用户权限集合
        }
        return new UsernamePasswordAuthenticationToken(username, token, new ArrayList<>());
    }
}
