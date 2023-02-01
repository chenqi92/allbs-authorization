package cn.allbs.allbsjwt.config.handler;

import cn.allbs.allbsjwt.config.enums.SystemCode;
import cn.allbs.common.utils.R;
import cn.allbs.common.utils.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类 Http401AuthenticationEntryPoint
 * </p>
 * 自定义需要权限认证的返回信息
 *
 * @author ChenQi
 * @since 2023/2/1 15:07
 */
public class Http401AuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseUtil.out(response, R.fail(SystemCode.FORBIDDEN_401));
    }
}
