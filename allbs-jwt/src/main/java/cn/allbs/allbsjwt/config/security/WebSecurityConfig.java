package cn.allbs.allbsjwt.config.security;

import cn.allbs.allbsjwt.config.filter.SecurityLoginFilter;
import cn.allbs.allbsjwt.config.filter.TokenAuthenticationFilter;
import cn.allbs.allbsjwt.config.grant.CustomDaoAuthenticationProvider;
import cn.allbs.allbsjwt.config.handler.*;
import cn.allbs.allbsjwt.config.security.service.CustomUserServiceImpl;
import cn.hutool.core.util.ArrayUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PermitAllUrlProperties permitAllUrlProperties;

    private final CustomUserServiceImpl customUserService;

    private final RedisTemplate<Object, Object> redisTemplate;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        // 防止iframe内容无法展示
        http.headers().frameOptions().disable();
        // 需要权限验证的提示code和文字说明自定义
        http.exceptionHandling().authenticationEntryPoint(new Http401AuthenticationEntryPoint());
        // 自定义403forbidden
        http.exceptionHandling().accessDeniedHandler(new Http403AccessDeniedEntryPoint());
        ExpressionUrlAuthorizationConfigurer<HttpSecurity>
                .ExpressionInterceptUrlRegistry registry = http
                .authorizeRequests();
        // 跨域检测
        registry.antMatchers(HttpMethod.OPTIONS, "/**").permitAll();
        // 忽略鉴权的请求
        permitAllUrlProperties.getIgnoreUrls().forEach(ignoreUrl -> registry.antMatchers(ignoreUrl).permitAll());
        permitAllUrlProperties.getIgnoreUrlsMap().forEach((k, v) -> registry.mvcMatchers(k, ArrayUtil.toArray(v, String.class)).permitAll());
        // 登出
        registry.and().logout().logoutUrl("/token/logout").addLogoutHandler(new SecurityLogoutHandler(redisTemplate))
                .deleteCookies("JSESSIONID")
                .logoutSuccessHandler(logoutSuccessHandler());
        // 登录
        registry.and().formLogin().loginPage("/login").permitAll();
        registry.and()
                // 登录并颁发token
                .addFilter(new SecurityLoginFilter(authenticationManager(), redisTemplate));
        // 对任何请求都进行权限验证
        registry.anyRequest().authenticated()
                .and().csrf().disable();
        registry.and()
                // 移除session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // 验证续签token
        registry.and().addFilterBefore(new TokenAuthenticationFilter(authenticationManager(), customUserService, redisTemplate), UsernamePasswordAuthenticationFilter.class);
        // @formatter:on
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 自定义身份认证器
        CustomDaoAuthenticationProvider daoAuthenticationProvider = new CustomDaoAuthenticationProvider();
        // 指定加密方式
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        // 用户账号密码、权限等信息获取
        daoAuthenticationProvider.setUserDetailsService(customUserService);
        auth.authenticationProvider(daoAuthenticationProvider);
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

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
