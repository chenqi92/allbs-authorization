package cn.allbs.allbsjwt.config.handler;

import cn.allbs.allbsjwt.config.enums.SystemCode;
import cn.allbs.common.utils.R;
import cn.allbs.common.utils.ResponseUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类 Http403AuthenticationEntryPoint
 * </p>
 *
 * @author ChenQi
 * @since 2023/3/2 16:12
 */
public class Http403AccessDeniedEntryPoint implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseUtil.out(response, R.fail(SystemCode.FORBIDDEN_403));
    }
}
