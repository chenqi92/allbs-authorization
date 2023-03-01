package cn.allbs.allbsjwt.config.filter;

import cn.allbs.allbsjwt.config.utils.TokenUtil;
import cn.allbs.allbsjwt.config.vo.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.allbs.allbsjwt.config.constant.CacheConstant.CACHE_TOKEN;

/**
 * 类 SecurityAuthenticationFilter
 * </p>
 * token校验和续订
 *
 * @author ChenQi
 * @since 2023/2/2 11:04
 */
@Slf4j
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;

    private final RedisTemplate<Object, Object> redisTemplate;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, RedisTemplate<Object, Object> redisTemplate) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = TokenUtil.getToken(request);
        if (!Boolean.TRUE.equals(redisTemplate.hasKey(CACHE_TOKEN + token))) {
            chain.doFilter(request, response);
            return;
        }
        // 如果token存在 则验证token是否正确和过期
        if (!TokenUtil.validateToken(token)) {
            // token 验证不通过
            chain.doFilter(request, response);
            return;
        }
        Authentication authentication = getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        String username = TokenUtil.getUsernameFromToken(token);
        if (StringUtils.hasText(username)) {
            // 查询当前用户权限集合,因为并没有将权限列表放在token中所以无法通过token解析出来，去数据库或者redis中获取,当然放在token中也是可以的
            SysUser userInfo = (SysUser) this.userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(userInfo, token, userInfo.getAuthorities());
        }
        return null;
    }
}
