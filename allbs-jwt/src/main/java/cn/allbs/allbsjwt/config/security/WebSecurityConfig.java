package cn.allbs.allbsjwt.config.security;

import cn.allbs.allbsjwt.config.handler.Http401AuthenticationEntryPoint;
import cn.allbs.allbsjwt.config.handler.PasswordLogoutSuccessHandler;
import cn.allbs.allbsjwt.config.handler.PermitAllUrlProperties;
import cn.allbs.allbsjwt.config.handler.SecurityLogoutHandler;
import cn.allbs.allbsjwt.config.security.service.CustomUserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 类 WebSecurityConfig
 * </p>
 * 自定义Security策略
 *
 * @author ChenQi
 * @since 2023/2/1 13:48
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PermitAllUrlProperties permitAllUrlProperties;

    private final CustomUserServiceImpl customUserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        // 防止iframe内容无法展示
        http.headers().frameOptions().disable();
        // 需要权限验证的提示code和文字说明自定义
        http.exceptionHandling().authenticationEntryPoint(new Http401AuthenticationEntryPoint());
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>
                .ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();
        // 跨域检测
        registry.antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        // 忽略鉴权的请求
        permitAllUrlProperties.getIgnoreUrls().forEach(ignoreUrl -> registry.antMatchers(ignoreUrl).permitAll());
        // 登出
        registry.and().logout().logoutUrl("/token/logout").addLogoutHandler(new SecurityLogoutHandler())
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler());
        // 登录
        registry.and().formLogin().loginPage("/token/login").permitAll();
        // 对任何请求都进行权限验证
        registry.anyRequest().authenticated()
                .and().csrf().disable();
        // @formatter:on
    }

    /**
     * 登出成功处理方法
     *
     * @return
     */
    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new PasswordLogoutSuccessHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService);
    }
}
