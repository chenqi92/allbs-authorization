package cn.allbs.allbsjwt.config.security;

import cn.allbs.allbsjwt.config.handler.PermitAllUrlProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;

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

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        // 防止iframe内容无法展示
        http.headers().frameOptions().disable();
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>
                .ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();
        // 跨域检测
        registry.antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        // 忽略鉴权的请求
        permitAllUrlProperties.getIgnoreUrls().forEach(ignoreUrl -> registry.antMatchers(ignoreUrl).permitAll());
        // 对任何请求都进行权限验证
        registry.anyRequest().authenticated()
                .and().csrf().disable();
        // @formatter:on
    }
}
