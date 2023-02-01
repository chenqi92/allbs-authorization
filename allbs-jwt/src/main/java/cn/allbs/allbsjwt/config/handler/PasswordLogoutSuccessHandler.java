package cn.allbs.allbsjwt.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * 类 PasswordSuccessHandler
 * </p>
 * 登出成功后执行的操作
 *
 * @author ChenQi
 * @since 2023/2/1 16:58
 */
@Slf4j
public class PasswordLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        if (authentication == null) {
            return;
        }
        // 获取请求参数中是否包含 回调地址
        log.info(String.format("IP %s，用户 %s， 于 %s 退出系统。", request.getRemoteHost(), authentication.getName(), LocalDateTime.now()));
        // 记录日志
        // 发送邮件提醒
    }
}
