package cn.allbs.allbsjwt.config.handler;

import cn.allbs.allbsjwt.config.constant.SecurityConstant;
import cn.allbs.allbsjwt.config.enums.SystemCode;
import cn.allbs.allbsjwt.config.utils.TokenUtil;
import cn.allbs.common.utils.ResponseUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static cn.allbs.allbsjwt.config.constant.CacheConstant.CACHE_TOKEN;
import static cn.allbs.allbsjwt.config.constant.SecurityConstant.BEARER_TYPE;

/**
 * 类 SecurityLogoutHandler
 * </p>
 * 自定义退出处理器
 *
 * @author ChenQi
 * @since 2023/2/1 16:30
 */
public class SecurityLogoutHandler implements LogoutHandler {

    private final RedisTemplate<Object, Object> redisTemplate;

    public SecurityLogoutHandler(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 从header 获取token
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        try {
            // 判断头部中是否含有以Bearer开头的token内容
            if (StrUtil.isEmpty(token) || !token.startsWith(BEARER_TYPE)) {
                ResponseUtil.write(response, SystemCode.FORBIDDEN_401);
                return;
            }
            // 解析登出用户名
            String username = TokenUtil.getUsernameFromToken(token.replace(SecurityConstant.BEARER_TYPE, StringPool.EMPTY));
            if (StrUtil.isEmpty(username)) {
                ResponseUtil.write(response, SystemCode.TOKEN_NOT_IN_SYSTEM);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        redisTemplate.delete(CACHE_TOKEN + token);
    }
}
