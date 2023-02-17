package cn.allbs.allbsjwt.config.filter;

import cn.allbs.allbsjwt.config.dto.LoginDTO;
import cn.allbs.allbsjwt.config.enums.SystemCode;
import cn.allbs.allbsjwt.config.grant.CustomJwtToken;
import cn.allbs.allbsjwt.config.utils.TokenUtil;
import cn.allbs.common.utils.R;
import cn.allbs.common.utils.ResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static cn.allbs.allbsjwt.config.constant.CacheConstant.CACHE_TOKEN;

/**
 * 类 SecurityLoginFilter
 * </p>
 * 登陆过滤器，检查输入的用户名和密码，并根据认证结果决定是否将这一结果传递给下一个过滤器。验证成功则颁发token
 *
 * @author ChenQi
 * @since 2023/2/2 10:56
 */
public class SecurityLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final RedisTemplate<Object, Object> redisTemplate;

    public SecurityLoginFilter(AuthenticationManager authenticationManager, RedisTemplate<Object, Object> redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.redisTemplate = redisTemplate;
    }

    /**
     * 尝试身份认证(接收并解析用户凭证)
     *
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginDTO user = new ObjectMapper().readValue(request.getInputStream(), LoginDTO.class);
        return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        user.getPassword(),
                        new ArrayList<>()
                ));
    }

    /**
     * 认证成功(用户成功登录后，这个方法会被调用，生成token)
     *
     * @param request
     * @param response
     * @param chain
     * @param auth
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        // 存储登录认证信息到上下文
        SecurityContextHolder.getContext().setAuthentication(auth);
        // 触发成功登录事件监听器
        if (this.eventPublisher != null) {
            eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(auth, this.getClass()));
        }
        // 生成并返回token给客户端，后续访问携带此token
        CustomJwtToken token = new CustomJwtToken(UUID.randomUUID().toString());
        String tokenStr = TokenUtil.generateToken(auth);
        token.setToken(tokenStr);
        token.setPermissions(auth.getAuthorities());
        // redis中储存token
        redisTemplate.opsForValue().set(CACHE_TOKEN + tokenStr, tokenStr, TokenUtil.EXPIRE_TIME, TimeUnit.MILLISECONDS);
        // 返回Token 相关信息
        ResponseUtil.out(response, R.ok(token));
        // 记录日志
    }

    /**
     * 认证失败调用
     *
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        // 使用的是自定义code:401003 所以在response中不能设置该自定义的code
        ResponseUtil.write(response, SystemCode.USERNAME_OR_PASSWORD_ERROR);
        // 记录日志
    }
}
