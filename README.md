# 系统权限
## 说明

- 功能权限（就是常用的RBAC那一套，登录->控制到按钮级别的权限系统）
- 数据权限 (根据不用用户，如一个园区分为多家企业，每家企业看到的数据内容不同，园区内不同领导分管不同的多家企业)

## 功能权限

### [源码](https://github.com/chenqi92/allbs-authorization.git)地址

### 权限框架

#### spring security

##### 自定义security策略，初步的权限校验，拦截所有的请求，swagger页面和接口无法访问

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                // 跨域检测
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 对任何请求都进行权限验证
                .anyRequest().authenticated()
                ;
    }
}
```

##### 指定页面放开

以swagger和阿里druid连接池监控工具为例，添加以下内容后，swagger内容将正常显示

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests()
                // 跨域检测
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 放行的url
                .antMatchers("/v3/api-docs/**", "/webjars/**", "/druid/**", "/configuration/ui", "/swagger-resources/**", "/css/**", "/js/**", "/plugins/**", "/favicon.ico", "/doc.html", "/static/**").permitAll()
                // 对任何请求都进行权限验证
                .anyRequest().authenticated()
        ;
        // @formatter:on
    }
}
```

![image-20230201141211528](https://mf.allbs.cn/cloudpic/2023/02/d277c2a17bf657778605feab47a593f2.png)

##### 将写死的需要放开的url添加至yml中

```yaml
# 配置的url
security:
  ignore-urls:
    - /v3/api-docs/**
    - /doc.html
    - /webjars/**
    - /druid/**
    - /static/**
    - /configuration/ui
    - /swagger-resources/**

```

```java
// 获取配置url内容
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnExpression("!'${security.ignore-urls}'.isEmpty()")
@ConfigurationProperties(prefix = "security")
public class PermitAllUrlProperties implements InitializingBean {

    private static final Pattern PATTERN = Pattern.compile("\\{(.*?)\\}");

    private final WebApplicationContext applicationContext;

    @Getter
    @Setter
    private List<String> ignoreUrls = new ArrayList<>();

    @Override
    public void afterPropertiesSet() throws Exception {
        RequestMappingHandlerMapping mapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> map = mapping.getHandlerMethods();

        map.keySet().forEach(info -> {
            HandlerMethod handlerMethod = map.get(info);
        });
    }
}
```

```java
// 在开放配置中添加上述配置的url
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
```

##### 自定义权限验证提示编码和提示文字

```java
// 枚举异常code
@Getter
@RequiredArgsConstructor
@ApiModel(description = "自定义异常code")
public enum SystemCode implements IResultCode {

    /**
     * 自定义异常code枚举
     */
    FORBIDDEN_401(401, "没有访问权限");

    /**
     * code编码
     */
    private final int code;
    /**
     * 中文信息描述
     */
    private final String msg;
}
```

```java
// 处理权限验证失败的处理类
public class Http401AuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseUtil.out(response, R.fail(SystemCode.FORBIDDEN_401));
    }
}
```

```java
// 将自定义处理添加至配置中
http.exceptionHandling().authenticationEntryPoint(new Http401AuthenticationEntryPoint());
```

![image-20230201151758035](https://mf.allbs.cn/cloudpic/2023/02/7dbf304076b2977703179080f93e3be2.png)

##### 与数据库联动

```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `permission` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `parent_id` int(11) NULL DEFAULT NULL COMMENT '父菜单ID',
  `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `sort` int(11) NULL DEFAULT 1 COMMENT '排序值',
  `keep_alive` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10013 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1000, '权限管理', NULL, '/user', -1, 'icon-quanxianguanli', 0, '0', '0', '2023-02-01 08:29:53', '2023-02-01 08:29:53', '0');
INSERT INTO `sys_menu` VALUES (1100, '用户管理', NULL, '/admin/user/index', 1000, 'icon-yonghuguanli', 1, '1', '1', '2023-02-01 08:29:53', '2023-02-02 09:38:50', '0');
INSERT INTO `sys_menu` VALUES (1101, '用户新增', 'sys_user_add', NULL, 1100, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:38:54', '0');
INSERT INTO `sys_menu` VALUES (1102, '用户修改', 'sys_user_edit', NULL, 1100, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:38:54', '0');
INSERT INTO `sys_menu` VALUES (1103, '用户删除', 'sys_user_del', NULL, 1100, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:38:54', '0');
INSERT INTO `sys_menu` VALUES (1200, '菜单管理', NULL, '/admin/menu/index', 1000, 'icon-caidanguanli', 2, '0', '1', '2023-02-01 08:29:53', '2023-02-02 09:39:39', '0');
INSERT INTO `sys_menu` VALUES (1201, '菜单新增', 'sys_menu_add', NULL, 1200, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:17', '0');
INSERT INTO `sys_menu` VALUES (1202, '菜单修改', 'sys_menu_edit', NULL, 1200, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:17', '0');
INSERT INTO `sys_menu` VALUES (1203, '菜单删除', 'sys_menu_del', NULL, 1200, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:17', '0');
INSERT INTO `sys_menu` VALUES (1300, '角色管理', NULL, '/admin/role/index', 1000, 'icon-jiaoseguanli', 3, '0', '1', '2023-02-01 08:29:53', '2023-02-02 09:39:38', '0');
INSERT INTO `sys_menu` VALUES (1301, '角色新增', 'sys_role_add', NULL, 1300, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:20', '0');
INSERT INTO `sys_menu` VALUES (1302, '角色修改', 'sys_role_edit', NULL, 1300, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:20', '0');
INSERT INTO `sys_menu` VALUES (1303, '角色删除', 'sys_role_del', NULL, 1300, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:20', '0');
INSERT INTO `sys_menu` VALUES (1304, '分配权限', 'sys_role_perm', NULL, 1300, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:20', '0');
INSERT INTO `sys_menu` VALUES (1400, '部门管理', NULL, '/admin/dept/index', 1000, 'icon-web-icon-', 4, '0', '1', '2023-02-01 08:29:53', '2023-02-02 09:39:36', '0');
INSERT INTO `sys_menu` VALUES (1401, '部门新增', 'sys_dept_add', NULL, 1400, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:22', '0');
INSERT INTO `sys_menu` VALUES (1402, '部门修改', 'sys_dept_edit', NULL, 1400, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:23', '0');
INSERT INTO `sys_menu` VALUES (1403, '部门删除', 'sys_dept_del', NULL, 1400, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:23', '0');
INSERT INTO `sys_menu` VALUES (1500, '租户管理', '', '/admin/tenant/index', 1000, 'icon-erji-zuhushouye', 5, '0', '1', '2023-02-01 08:29:53', '2023-02-02 09:39:34', '0');
INSERT INTO `sys_menu` VALUES (1501, '租户新增', 'admin_systenant_add', NULL, 1500, '1', 0, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:25', '0');
INSERT INTO `sys_menu` VALUES (1502, '租户修改', 'admin_systenant_edit', NULL, 1500, '1', 1, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:25', '0');
INSERT INTO `sys_menu` VALUES (1503, '租户删除', 'admin_systenant_del', NULL, 1500, '1', 2, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:25', '0');
INSERT INTO `sys_menu` VALUES (2000, '系统管理', NULL, '/admin', -1, 'icon-xitongguanli', 1, '0', '1', '2023-02-01 08:29:53', '2023-02-02 09:39:33', '0');
INSERT INTO `sys_menu` VALUES (2100, '日志管理', NULL, '/admin/log/index', 2000, 'icon-rizhiguanli', 5, '0', '1', '2023-02-01 08:29:53', '2023-02-02 09:39:33', '0');
INSERT INTO `sys_menu` VALUES (2101, '日志删除', 'sys_log_del', NULL, 2100, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:27', '0');
INSERT INTO `sys_menu` VALUES (2200, '字典管理', NULL, '/admin/dict/index', 2000, 'icon-navicon-zdgl', 6, '0', '1', '2023-02-01 08:29:53', '2023-02-02 09:39:30', '0');
INSERT INTO `sys_menu` VALUES (2201, '字典删除', 'sys_dict_del', NULL, 2200, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:29', '0');
INSERT INTO `sys_menu` VALUES (2202, '字典新增', 'sys_dict_add', NULL, 2200, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:29', '0');
INSERT INTO `sys_menu` VALUES (2203, '字典修改', 'sys_dict_edit', NULL, 2200, NULL, NULL, '0', '2', '2023-02-01 08:29:53', '2023-02-02 09:39:29', '0');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `role_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ds_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '2',
  `ds_scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0),
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0),
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
  PRIMARY KEY (`role_id`) USING BTREE,
  INDEX `role_idx1_role_code`(`role_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '管理员', 'ROLE_ADMIN', '管理员', '0', '2', '2023-02-01 15:45:51', '2023-02-01 14:09:11', '0');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `menu_id` int(11) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1000);
INSERT INTO `sys_role_menu` VALUES (1, 1100);
INSERT INTO `sys_role_menu` VALUES (1, 1101);
INSERT INTO `sys_role_menu` VALUES (1, 1102);
INSERT INTO `sys_role_menu` VALUES (1, 1103);
INSERT INTO `sys_role_menu` VALUES (1, 1200);
INSERT INTO `sys_role_menu` VALUES (1, 1201);
INSERT INTO `sys_role_menu` VALUES (1, 1202);
INSERT INTO `sys_role_menu` VALUES (1, 1203);
INSERT INTO `sys_role_menu` VALUES (1, 1300);
INSERT INTO `sys_role_menu` VALUES (1, 1301);
INSERT INTO `sys_role_menu` VALUES (1, 1302);
INSERT INTO `sys_role_menu` VALUES (1, 1303);
INSERT INTO `sys_role_menu` VALUES (1, 1304);
INSERT INTO `sys_role_menu` VALUES (1, 1400);
INSERT INTO `sys_role_menu` VALUES (1, 1401);
INSERT INTO `sys_role_menu` VALUES (1, 1402);
INSERT INTO `sys_role_menu` VALUES (1, 1403);
INSERT INTO `sys_role_menu` VALUES (1, 1500);
INSERT INTO `sys_role_menu` VALUES (1, 1501);
INSERT INTO `sys_role_menu` VALUES (1, 1502);
INSERT INTO `sys_role_menu` VALUES (1, 1503);
INSERT INTO `sys_role_menu` VALUES (1, 2000);
INSERT INTO `sys_role_menu` VALUES (1, 2100);
INSERT INTO `sys_role_menu` VALUES (1, 2101);
INSERT INTO `sys_role_menu` VALUES (1, 2200);
INSERT INTO `sys_role_menu` VALUES (1, 2201);
INSERT INTO `sys_role_menu` VALUES (1, 2202);
INSERT INTO `sys_role_menu` VALUES (1, 2203);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `salt` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `dept_id` int(11) NULL DEFAULT NULL COMMENT '部门ID',
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `lock_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
  `del_flag` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `user_idx1_username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$IVzj1Wd.ZQdOIWdb1htQjexU94uoNeuk1crlQ9ExVupPi0Iy1uv.C', '', '13812345678', '/admin/sys-file/2023/01/3003703388943b1e2be6dc2e78781fc3.png', 1, '2023-02-01 07:15:18', '2023-02-01 16:45:23', '0', '0');
INSERT INTO `sys_user` (user_id, username, password, salt, phone, avatar, dept_id, create_time, update_time, lock_flag, del_flag) VALUES (2, 'test', '$2a$10$IVzj1Wd.ZQdOIWdb1htQjexU94uoNeuk1crlQ9ExVupPi0Iy1uv.C', null, '13812348765', 'sdsdfwew', 1, '2023-03-01 09:35:31', '2023-03-01 09:35:34', '0', '0');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` (user_id, role_id) VALUES (2, 1);

SET FOREIGN_KEY_CHECKS = 1;
```

##### token相关

```xml
<!-- 使用jar包jjwt -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
```

其他若干工具类等直接见源代码即可

##### 用户登出

```java
// 登出
registry.and().logout().logoutUrl("/token/logout").addLogoutHandler(new SecurityLogoutHandler())
    .deleteCookies("JSESSIONID")
    .logoutSuccessHandler(logoutSuccessHandler());
```

```java
@Bean
public LogoutSuccessHandler logoutSuccessHandler() {
    return new PasswordLogoutSuccessHandler();
}
```

添加自定义类`SecurityLogoutHandler`继承`LogoutHandler`处理登出时的逻辑，如删除redis中的token缓存记录等。

添加自定义类`PasswordLogoutSuccessHandler`实现`LogoutSuccessHandler`处理登出成功后的逻辑，比如跳转指定页面、记录日志、发送邮件通知等

##### 用户登录
自定义用户信息类继承`org.springframework.security.core.userdetails.User`

```java
public class SysUser extends User {

    /**
     * 用户ID
     */
    @Getter
    private final Long id;
    /**
     * 手机号
     */
    @Getter
    private final String phone;

    /**
     * 头像
     */
    @Getter
    private final String avatar;

    /**
     * 是否为网格员片区总管(0:不是,1:是)
     */
    @Getter
    @Setter
    private Integer isGridHeadUser;

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    public SysUser(Long id, String phone, String avatar, String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.id = id;
        this.phone = phone;
        this.avatar = avatar;
    }
}
```
查询权限集合、用户信息、角色列表等信息构建SysUser

```java
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {

    private final SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = sysUserService.findUserInfoByUserName(username);
        if (userInfo == null) {
            throw new UsernameNotFoundException("指定用户不存在!");
        }
        // 权限标识的集合
        Set<String> dbAuthsSet = new HashSet<>(Arrays.asList(userInfo.getPermissions()));
        Collection<? extends GrantedAuthority> authorities
                = AuthorityUtils.createAuthorityList(dbAuthsSet.toArray(new String[0]));
        SysUserEntity user = userInfo.getSysUser();
        // 判断用户是否为正常使用的状态
        boolean enabled = StrUtil.equals(user.getLockFlag(), SecurityConstant.STATUS_NORMAL);
        // @formatter:off
        return new SysUser(
                // 用户id
                user.getUserId(),
                // 用户手机号
                user.getPhone(),
                // 用户头像
                user.getAvatar(),
                // 用户名
                user.getUsername(),
                // 密码
                user.getPassword(),
                // 用户账号是否为正常使用的状态
                enabled,
                true,
                true,
                // 判断用户是否为锁定状态
                !SecurityConstant.STATUS_LOCK.equals(user.getLockFlag()),
                // 权限列表
                authorities
        );
        // @formatter:on
    }
}
```

自定义登录后返回的字段和内容，自定义一个类`CustomJwtToken`

```java
public class CustomJwtToken implements Serializable {

    private static final long serialVersionUID = 2149134569530465633L;

    @JsonIgnore
    private String value;

    /**
     * token
     */
    private String token;

    private String tokenType = BEARER_TYPE.toLowerCase();

    /**
     * 权限集合
     */
    private Set<String> permissions;

    public CustomJwtToken(String value) {
        this.value = value;
    }

    @SuppressWarnings("unused")
    private CustomJwtToken() {
        this((String) null);
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * The token value.
     *
     * @return The token value.
     */
    public String getValue() {
        return value;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && toString().equals(obj.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Set<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(Collection<? extends GrantedAuthority> authorities) {
        this.permissions = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }
}
```

自定义一个身份认证器，并在其中自定义整个登录认证逻辑

```java
@Slf4j
public class CustomDaoAuthenticationProvider extends DaoAuthenticationProvider {

    /**
     * The plaintext password used to perform PasswordEncoder#matches(CharSequence,
     * String)} on when the user is not found to avoid SEC-2056.
     */
    private static final String USER_NOT_FOUND_PASSWORD = "userNotFoundPassword";

    private PasswordEncoder passwordEncoder;

    /**
     * The password used to perform {@link PasswordEncoder#matches(CharSequence, String)}
     * on when the user is not found to avoid SEC-2056. This is necessary, because some
     * {@link PasswordEncoder} implementations will short circuit if the password is not
     * in a valid format.
     */
    private volatile String userNotFoundEncodedPassword;

    private UserDetailsService userDetailsService;

    private UserDetailsPasswordService userDetailsPasswordService;

    /**
     * user 属性校验
     */
    @Setter
    private UserDetailsChecker preAuthenticationChecks = new AccountStatusUserDetailsChecker();

    public CustomDaoAuthenticationProvider() {
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 可以在此处覆写整个登录认证逻辑
        if (authentication.getCredentials() == null) {
            log.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException("Bad credentials");
        }

        // 手机号
        String userName = authentication.getName();
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(userName);

        // 校验账号是否禁用
        preAuthenticationChecks.check(userDetails);

        // 账号密码校验
        additionalAuthenticationChecks(userDetails,
                (UsernamePasswordAuthenticationToken) authentication);

        // 提供用户名、密码、权限列表供SecurityLoginFilter使用
        return new UsernamePasswordAuthenticationToken(userName, userDetails.getPassword(), userDetails.getAuthorities());
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        // 可以在此覆写整个密码校验逻辑
        if (authentication.getCredentials() == null) {
            this.logger.debug("Failed to authenticate since no credentials provided");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!this.passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            this.logger.debug("Failed to authenticate since password does not match stored value");
            throw new BadCredentialsException(this.messages
                    .getMessage("AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
        }
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication,
                                                         UserDetails user) {
        boolean upgradeEncoding = this.userDetailsPasswordService != null
                && this.passwordEncoder.upgradeEncoding(user.getPassword());
        if (upgradeEncoding) {
            String presentedPassword = authentication.getCredentials().toString();
            String newPassword = this.passwordEncoder.encode(presentedPassword);
            user = this.userDetailsPasswordService.updatePassword(user, newPassword);
        }
        return super.createSuccessAuthentication(principal, authentication, user);
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate passwords. If
     * not set, the password will be compared using
     * {@link PasswordEncoderFactories#createDelegatingPasswordEncoder()}
     *
     * @param passwordEncoder must be an instance of one of the {@code PasswordEncoder}
     *                        types.
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        Assert.notNull(passwordEncoder, "passwordEncoder cannot be null");
        this.passwordEncoder = passwordEncoder;
        this.userNotFoundEncodedPassword = null;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return this.passwordEncoder;
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    protected UserDetailsService getUserDetailsService() {
        return this.userDetailsService;
    }

    public void setUserDetailsPasswordService(UserDetailsPasswordService userDetailsPasswordService) {
        this.userDetailsPasswordService = userDetailsPasswordService;
    }
}
```

将自定义的身份认证逻辑设置进`WebSecurityConfig`策略中

```java
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
```

添加登陆过滤器，检查输入的用户名和密码，并根据认证结果决定是否将这一结果传递给下一个过滤器。验证成功则颁发token

```java
public class SecurityLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public SecurityLoginFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
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
        token.setToken(TokenUtil.generateToken(auth));
        token.setPermissions(auth.getAuthorities());
        // TODO 储存redis
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
```

添加过滤器验证需要验证用户请求时所带的token是否正确

```java
package cn.allbs.allbsjwt.config.filter;

import cn.allbs.allbsjwt.config.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

    private final UserDetailsService userDetailsService;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = TokenUtil.getToken(request);
        // 如果token存在 则验证token是否正确和过期 TODO 去redis中判断token是否存在
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
            UserDetails userInfo = this.userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(username, token, userInfo.getAuthorities());
        }
        return null;
    }
}
```

在spring security配置文件中添加该过滤器

```java
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
    registry.and().formLogin().loginPage("/login").permitAll();
    registry.and()
            // 登录并颁发token
            .addFilter(new SecurityLoginFilter(authenticationManager()));
    // 对任何请求都进行权限验证
    registry.anyRequest().authenticated()
            .and().csrf().disable();
    registry.and()
            // 移除session
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    // 验证续签token
    registry.and().addFilterBefore(new TokenAuthenticationFilter(authenticationManager(), customUserService), UsernamePasswordAuthenticationFilter.class);
    // @formatter:on
}
```

至此，从数据库中登录，获取用户名、密码、菜单、权限，登出功能已完成

![](https://mf.allbs.cn/cloudpic/2023/02/bb6822021c3730ef3d8dbbf1859752bd.gif)

##### 资源访问权限
新增`PermissionService`用于接口指定访问权限

在`WebSecurityConfig`中添加注解`@EnableGlobalMethodSecurity(prePostEnabled=true)`

![image-20230302160327406](https://mf.allbs.cn/cloudpic/2023/03/bd222e8004847bfde5cfd7092aab9b87.png)

如果用户的权限列表中不包含该权限则不允许访问。一般用于按钮级别权限的控制。

```java
package com.lyc.admin.oauth.service;

import cn.hutool.core.util.ArrayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

@Slf4j
@Component("pms")
public class PermissionService {
    /**
     * 判断接口是否有任意xxx，xxx权限
     *
     * @param permissions 权限
     * @return {boolean}
     */
    public boolean hasPermission(String... permissions) {
        if (ArrayUtil.isEmpty(permissions)) {
            return false;
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(StringUtils::hasText)
                .anyMatch(x -> PatternMatchUtils.simpleMatch(permissions, x));
    }
}
```

测试，添加两个接口，一个接口含有permission中的字符串，一个没有。@PreAuthorize 使用时如果没有权限将不会进入方法。

![image-20230302160427598](https://mf.allbs.cn/cloudpic/2023/03/66a7af9d2d6db52a8a0b80afac93a949.png)

![image-20230302160500380](https://mf.allbs.cn/cloudpic/2023/03/9e96c08575f5a272b38a4484c617861c.png)

postman调用结果

![image-20230302160533945](https://mf.allbs.cn/cloudpic/2023/03/6e8df2153d95a39b6434d6e295a8c49a.png)

![image-20230302160548646](https://mf.allbs.cn/cloudpic/2023/03/002f7b00850d1036adddb2e5c043706e.png)

统一403返回格式

```java
// enum
FORBIDDEN_403(403, "缺少资源访问权限!"),

// 403自定义处理
public class Http403AccessDeniedEntryPoint implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        ResponseUtil.out(response, R.fail(SystemCode.FORBIDDEN_403));
    }
}

// 加入配置
HttpSecurity.exceptionHandling().accessDeniedHandler(new Http403AccessDeniedEntryPoint());
```

![image-20230302162022243](https://mf.allbs.cn/cloudpic/2023/03/0e569ff7a3d2730bb618dfb7d2fb35cc.png)

权限控制 没有权限,方法依然执行但是不会返回

![image-20230302163807705](https://mf.allbs.cn/cloudpic/2023/03/6966cdb50606ced2d3529cc51e2d57dd.png)

使用场景，比如只允许用户查询自己的用户信息。

![image-20230302171653246](https://mf.allbs.cn/cloudpic/2023/03/5781e2d0aa4661c45b2f97c7f07877bc.png)

![image-20230302171749403](https://mf.allbs.cn/cloudpic/2023/03/1897a99cdcc0041dce45f3ad01d8f91a.png)

对返回结果进行过滤

![image-20230302163927147](https://mf.allbs.cn/cloudpic/2023/03/6d1082c22c015cff5389657d2608ade8.png)

对请求参数进行过滤

![image-20230302170553274](https://mf.allbs.cn/cloudpic/2023/03/6dde253509e5a33750f0f3eeac149e35.png)

#### oauth2.0

## 数据权限

### 实现方式

以下使用的方式是拦截sql后自定义组装来实现数据过滤

### 代码实现1

#### 定义一个注解`DataScope`用于判断哪些类、方法需要进行数据过滤。当然你也可以稍微修改下，默认对数据进行过滤，指定类、方法不过滤

```java
package cn.allbs.allbsjwt.config.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

@Target({METHOD, TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    /**
     * 当进行过滤时主表中代表企业id的字段
     */
    String unitField() default "ent_id";

    /**
     * 是否进行数据过滤
     */
    boolean filterData() default true;

    /**
     * 忽略的表名
     *
     * @return 不进行数据过滤的表名的集合
     */
    String[] ignoreTables() default {"sys_file"};
}
```

#### 定义一个实体类用户临时储存sql过滤中需要使用的参数信息

```java
@Data
@EqualsAndHashCode(callSuper = true)
@RequiredArgsConstructor
public class DataScopeParam extends HashMap {

    /**
     * 企业筛选字段名称（比如某个表中并未使用其他表通用的字段ent_id进行区分企业）
     */
    private String unitField;

    /**
     * 企业数据范围
     */
    private Set<Long> entIdList;

    /**
     * 是否进行拦截
     */
    private boolean filterField;

    /**
     * 忽略不过滤的表名
     */
    private List<String> ignoreTables;
}

```

#### 权限解析器

```java
@Slf4j
public class DataScopeAnnotationClassResolver {

    /**
     * 缓存方法对应的权限拦截
     */
    private final Map<Object, DataScopeParam> dsCache = new ConcurrentHashMap<>();

    public DataScopeAnnotationClassResolver() {
    }

    /**
     * 从缓存获取数据
     *
     * @param method       方法
     * @param targetObject 目标对象
     * @return ds
     */
    public DataScopeParam findKey(Method method, Object targetObject) {
        if (method.getDeclaringClass() == Object.class) {
            return null;
        }
        Object cacheKey = new MethodClassKey(method, targetObject.getClass());
        DataScopeParam dsp = this.dsCache.get(cacheKey);
        if (dsp == null) {
            dsp = computeDatasource(method, targetObject);
            this.dsCache.put(cacheKey, dsp);
        }
        return dsp;
    }

    /**
     * 查找注解的顺序
     * 1. 当前方法
     * 2. 桥接方法
     * 3. 当前类开始一直找到Object
     *
     * @param method       方法
     * @param targetObject 目标对象
     * @return ds
     */
    private DataScopeParam computeDatasource(Method method, Object targetObject) {
        if (!Modifier.isPublic(method.getModifiers())) {
            return null;
        }
        //1. 从当前方法接口中获取
        DataScopeParam dsAttr = findDataSourceAttribute(method);
        if (dsAttr != null) {
            return dsAttr;
        }
        Class<?> targetClass = targetObject.getClass();
        Class<?> userClass = ClassUtils.getUserClass(targetClass);
        // JDK代理时,  获取实现类的方法声明.  method: 接口的方法, specificMethod: 实现类方法
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);

        specificMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        //2. 从桥接方法查找
        dsAttr = findDataSourceAttribute(specificMethod);
        if (dsAttr != null) {
            return dsAttr;
        }
        // 从当前方法声明的类查找
        dsAttr = findDataSourceAttribute(userClass);
        if (dsAttr != null && ClassUtils.isUserLevelMethod(method)) {
            return dsAttr;
        }
        //since 3.4.1 从接口查找，只取第一个找到的
        for (Class<?> interfaceClazz : ClassUtils.getAllInterfacesForClassAsSet(userClass)) {
            dsAttr = findDataSourceAttribute(interfaceClazz);
            if (dsAttr != null) {
                return dsAttr;
            }
        }
        // 如果存在桥接方法
        if (specificMethod != method) {
            // 从桥接方法查找
            dsAttr = findDataSourceAttribute(method);
            if (dsAttr != null) {
                return dsAttr;
            }
            // 从桥接方法声明的类查找
            dsAttr = findDataSourceAttribute(method.getDeclaringClass());
            if (dsAttr != null && ClassUtils.isUserLevelMethod(method)) {
                return dsAttr;
            }
        }
        return getDefaultDataSourceAttr(targetObject);
    }

    /**
     * 默认的获取
     *
     * @param targetObject 目标对象
     * @return DataScopeParam
     */
    private DataScopeParam getDefaultDataSourceAttr(Object targetObject) {
        Class<?> targetClass = targetObject.getClass();
        // 如果不是代理类, 从当前类开始, 不断的找父类的声明
        if (!Proxy.isProxyClass(targetClass)) {
            Class<?> currentClass = targetClass;
            while (currentClass != Object.class) {
                DataScopeParam datasourceAttr = findDataSourceAttribute(currentClass);
                if (datasourceAttr != null) {
                    return datasourceAttr;
                }
                currentClass = currentClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 通过 AnnotatedElement 查找标记的注解, 映射为  DatasourceHolder
     *
     * @param ae AnnotatedElement
     * @return 数据源映射持有者
     */
    private DataScopeParam findDataSourceAttribute(AnnotatedElement ae) {
        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(ae, DataScope.class);
        DataScopeParam dsp = null;
        if (attributes != null) {
            dsp = new DataScopeParam(attributes.getString("unitField"), new HashSet<>(), attributes.getBoolean("filterData"), Convert.toList(String.class, attributes.get("ignoreTables")));
        }
        return dsp;
    }
}
```

#### 处理自定义切面

```java
public class DataScopeAnnotationAdvisor extends AbstractPointcutAdvisor implements BeanFactoryAware {

    private final Advice advice;

    private final Pointcut pointcut;

    private final Class<? extends Annotation> annotation;

    public DataScopeAnnotationAdvisor(@NonNull MethodInterceptor advice,
                                      @NonNull Class<? extends Annotation> annotation) {
        this.advice = advice;
        this.annotation = annotation;
        this.pointcut = buildPointcut();
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

    @Override
    public Advice getAdvice() {
        return this.advice;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        if (this.advice instanceof BeanFactoryAware) {
            ((BeanFactoryAware) this.advice).setBeanFactory(beanFactory);
        }
    }

    private Pointcut buildPointcut() {
        Pointcut cpc = new AnnotationMatchingPointcut(annotation, true);
        Pointcut mpc = new AnnotationMethodPoint(annotation);
        return new ComposablePointcut(cpc).union(mpc);
    }

    /**
     * In order to be compatible with the spring lower than 5.0
     */
    private static class AnnotationMethodPoint implements Pointcut {

        private final Class<? extends Annotation> annotationType;

        public AnnotationMethodPoint(Class<? extends Annotation> annotationType) {
            Assert.notNull(annotationType, "Annotation type must not be null");
            this.annotationType = annotationType;
        }

        @Override
        public ClassFilter getClassFilter() {
            return ClassFilter.TRUE;
        }

        @Override
        public MethodMatcher getMethodMatcher() {
            return new AnnotationMethodMatcher(annotationType);
        }

        private static class AnnotationMethodMatcher extends StaticMethodMatcher {
            private final Class<? extends Annotation> annotationType;

            public AnnotationMethodMatcher(Class<? extends Annotation> annotationType) {
                this.annotationType = annotationType;
            }

            @Override
            public boolean matches(Method method, Class<?> targetClass) {
                if (matchesMethod(method)) {
                    return true;
                }
                // Proxy classes never have annotations on their redeclared methods.
                if (Proxy.isProxyClass(targetClass)) {
                    return false;
                }
                // The method may be on an interface, so let's check on the target class as well.
                Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
                return (specificMethod != method && matchesMethod(specificMethod));
            }

            private boolean matchesMethod(Method method) {
                return AnnotatedElementUtils.hasAnnotation(method, this.annotationType);
            }
        }
    }
}
```

#### 添加`DataScopeParamContentHolder`用户储存需要过滤数据的配置信息

```java
import com.alibaba.ttl.TransmittableThreadLocal;

public final class DataScopeParamContentHolder {

    private DataScopeParamContentHolder() {
    }

    private static final ThreadLocal<DataScopeParam> THREAD_PMS_HOLDER = new TransmittableThreadLocal<>();

    /**
     * 设置当前header中的权限
     *
     * @param dataScopeParam 需要过滤的权限
     */
    public static void set(DataScopeParam dataScopeParam) {
        THREAD_PMS_HOLDER.set(dataScopeParam);
    }

    /**
     * 获取header中的权限
     *
     * @return 权限
     */
    public static DataScopeParam get() {
        return THREAD_PMS_HOLDER.get();
    }

    public static void clear() {
        THREAD_PMS_HOLDER.remove();
    }
}
```

#### 添加拦截器`DataScopeAnnotationIntercept`用于拦截方法级别的注解，处理添加了上方自定义注解的方法,缓存自定义注解中的配置

```java
public class DataScopeAnnotationIntercept implements MethodInterceptor {

    private final DataScopeAnnotationClassResolver dataScopeAnnotationClassResolver;

    public DataScopeAnnotationIntercept() {
        dataScopeAnnotationClassResolver = new DataScopeAnnotationClassResolver();
    }

    @Nullable
    @Override
    public Object invoke(@NotNull MethodInvocation methodInvocation) throws Throwable {
        DataScopeParam paramKey = dataScopeAnnotationClassResolver.findKey(methodInvocation.getMethod(), methodInvocation.getThis());
        DataScopeParamContentHolder.set(paramKey);
        try {
            return methodInvocation.proceed();
        } finally {
            DataScopeParamContentHolder.clear();
        }
    }
}
```

#### 添加配置将上述内容注册进去

```java
@Configuration
public class DataScopeInitConfig {

    @Bean
    public Advisor generateAllDataScopeAdvisor() {
        DataScopeAnnotationIntercept intercept = new DataScopeAnnotationIntercept();
        DataScopeAnnotationAdvisor advisor = new DataScopeAnnotationAdvisor(intercept, DataScope.class);
        advisor.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return advisor;
    }
}
```

#### 最后根据配置内容信息对应的处理sql，这个类是关键，前面所有内容都是为他服务，如果只是想简单点写死代码直接使用该类就可以了

```java
package cn.allbs.allbsjwt.config.datascope;

import cn.allbs.allbsjwt.config.exception.UnauthorizedException;
import cn.allbs.allbsjwt.config.utils.SecurityUtils;
import cn.allbs.allbsjwt.config.vo.SysUser;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.io.StringReader;
import java.sql.Connection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

@Aspect
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
@Component
public class UnitDataPermissionInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 在有权限的情况下查询用户所关联的企业列表
        SysUser sysUser = SecurityUtils.getUser();
        // 如果非权限用户则不往下执行
        if (sysUser == null) {
            return invocation.proceed();
        }

        DataScopeParam dataScopeParam = DataScopeParamContentHolder.get();

        if (dataScopeParam != null) {
            dataScopeParam.setEntIdList(sysUser.getEntIdList());
        }

        // 获取header中的待过滤的企业列表
//        Set<Long> entIdList = CurrentEntIdSearchContextHolder.getEntIdList();
//        if (entIdList != null) {
//            if (dataScopeParam == null) {
//                dataScopeParam = new DataScopeParam("ent_id", entIdList, true, CollUtil.newArrayList("sys_file"));
//            } else {
//                // 查询交集
//                Set<Long> permissionEntList = dataScopeParam.getEntIdList();
//                dataScopeParam.setFilterField(true);
//                dataScopeParam.setEntIdList(entIdList.stream().filter(permissionEntList::contains).collect(Collectors.toSet()));
//            }
//        }

        // 没有添加注解则不往下执行
        if (dataScopeParam == null) {
            return invocation.proceed();
        }

        // 注解配置不过滤数据则不往下执行
        if (!dataScopeParam.isFilterField()) {
            return invocation.proceed();
        }

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        // 先判断是不是SELECT操作 不是直接过滤
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (SqlCommandType.FLUSH.equals(mappedStatement.getSqlCommandType()) || SqlCommandType.UNKNOWN.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }

        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        // 执行的SQL语句
        String originalSql = boundSql.getSql();
        // SQL语句的参数
        Object parameterObject = boundSql.getParameterObject();
        // 拦截插入语句
        if (SqlCommandType.INSERT.equals(mappedStatement.getSqlCommandType())) {
            // 当为insert时将判断是否具备权限
            if (parameterObject != null) {
                Long entId = Convert.toLong(ReflectUtil.getFieldValue(parameterObject, StrUtil.toCamelCase(dataScopeParam.getUnitField())));
                // 判断entId是否在权限范围内
                if (entId != null && !dataScopeParam.getEntIdList().contains(entId)) {
                    throw new UnauthorizedException("entId不在权限范围内");
                }
            }
            return invocation.proceed();
        }
        // 拦截更新语句，业务包含逻辑删除所以此处用的update
        if (SqlCommandType.UPDATE.equals(mappedStatement.getSqlCommandType())) {
            // 修改updateSql
            String updateSql = handleUpdateSql(originalSql, dataScopeParam.getEntIdList(), dataScopeParam.getUnitField(), dataScopeParam.getIgnoreTables());
            log.warn("数据权限处理过后UPDATE的SQL: {}", updateSql);
            metaObject.setValue("delegate.boundSql.sql", updateSql);
            return invocation.proceed();
        }
        // 需要过滤的数据
        String finalSql = this.handleSql(originalSql, dataScopeParam.getEntIdList(), dataScopeParam.getUnitField(), dataScopeParam.getIgnoreTables());
        log.warn("数据权限处理过后SELECT的SQL: {}", finalSql);

        // 装载改写后的sql
        metaObject.setValue("delegate.boundSql.sql", finalSql);
        return invocation.proceed();
    }


    /**
     * 修改select语句sql
     *
     * @param originalSql 原始sql
     * @param entIdList   需要过滤的企业列表
     * @param fieldName   当前主表中字段名称
     * @return 修改后的语句
     * @throws JSQLParserException sql修改异常
     */
    private String handleSql(String originalSql, Set<Long> entIdList, String fieldName, List<String> ignores) throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Select select = (Select) parserManager.parse(new StringReader(originalSql));
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            this.setWhere((PlainSelect) selectBody, entIdList, fieldName, ignores);
        } else if (selectBody instanceof SetOperationList) {
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selectBodyList = setOperationList.getSelects();
            selectBodyList.forEach(s -> this.setWhere((PlainSelect) s, entIdList, fieldName, ignores));
        }
        return select.toString();
    }

    /**
     * 修改update语句
     *
     * @param originalSql 元素sql
     * @param entIdList   允许查询的企业列表
     * @param fieldName   表中待过滤查询的列名
     * @param ignores     忽略的表名
     * @return
     * @throws JSQLParserException
     */
    private String handleUpdateSql(String originalSql, Set<Long> entIdList, String fieldName, List<String> ignores) throws JSQLParserException {
        CCJSqlParserManager parserManager = new CCJSqlParserManager();
        Update update = (Update) parserManager.parse(new StringReader(originalSql));
        if (ignores.contains(update.getTable().getName())) {
            // 当前表名的处于不过滤列表则不进行二次封装处理
            return originalSql;
        }
        String dataPermissionSql;
        if (entIdList.size() == 1) {
            EqualsTo selfEqualsTo = new EqualsTo();
            selfEqualsTo.setLeftExpression(new Column(fieldName));
            selfEqualsTo.setRightExpression(new LongValue(entIdList.stream().findFirst().orElse(0L)));
            dataPermissionSql = selfEqualsTo.toString();
        } else {
            dataPermissionSql = fieldName + " in ( " + CollUtil.join(entIdList, StringPool.COMMA) + " )";
        }
        update.setWhere(new AndExpression(update.getWhere(), CCJSqlParserUtil.parseCondExpression(dataPermissionSql)));
        return update.toString();
    }

    /**
     * 设置 where 条件  --  使用CCJSqlParser将原SQL进行解析并改写
     *
     * @param plainSelect 查询对象
     */
    @SneakyThrows(Exception.class)
    protected void setWhere(PlainSelect plainSelect, Set<Long> entIdList, String fieldName, List<String> ignores) {
        Table fromItem = (Table) plainSelect.getFromItem();
        // 有别名用别名，无别名用表名，防止字段冲突报错
        Alias fromItemAlias = fromItem.getAlias();
        if (ignores.contains(fromItem.getName())) {
            // 当前表名的处于不过滤列表则不进行二次封装处理
            return;
        }
        String mainTableName = fromItemAlias == null ? fromItem.getName() : fromItemAlias.getName();
        // 构建子查询 -- 数据权限过滤SQL
        String dataPermissionSql;
        if (entIdList.size() == 1) {
            EqualsTo selfEqualsTo = new EqualsTo();
            selfEqualsTo.setLeftExpression(new Column(mainTableName + "." + fieldName));
            selfEqualsTo.setRightExpression(new LongValue(entIdList.stream().findFirst().orElse(0L)));
            dataPermissionSql = selfEqualsTo.toString();
        } else if (entIdList.size() < 1) {
            dataPermissionSql = mainTableName + "." + fieldName + " in ( " + StringPool.NULL + " )";
        } else {
            dataPermissionSql = mainTableName + "." + fieldName + " in ( " + CollUtil.join(entIdList, StringPool.COMMA) + " )";
        }

        if (plainSelect.getWhere() == null) {
            plainSelect.setWhere(CCJSqlParserUtil.parseCondExpression(dataPermissionSql));
        } else {
            plainSelect.setWhere(new AndExpression(plainSelect.getWhere(), CCJSqlParserUtil.parseCondExpression(dataPermissionSql)));
        }
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {
        log.info(properties.toString());
    }
}

```

#### 新建几张表并插入数据用于数据测试

##### 企业信息表`cm_enterprise`

```mysql
create table cm_enterprise
(
    id          bigint                             not null comment '主键'
        primary key,
    name        varchar(255)                       not null comment '企业名称',
    create_time datetime default CURRENT_TIMESTAMP not null,
    update_time datetime                           not null on update CURRENT_TIMESTAMP,
    del_flag    char     default '0'               not null
)
    comment '企业信息表';
INSERT INTO authorization_demo.cm_enterprise (id, name, create_time, update_time, del_flag) VALUES (1, '企业1', '2023-03-01 09:33:48', '2023-03-01 09:33:58', '0');
INSERT INTO authorization_demo.cm_enterprise (id, name, create_time, update_time, del_flag) VALUES (2, '企业2', '2023-03-01 09:33:50', '2023-03-01 09:33:59', '0');
INSERT INTO authorization_demo.cm_enterprise (id, name, create_time, update_time, del_flag) VALUES (3, '企业3', '2023-03-01 09:33:55', '2023-03-01 09:34:00', '0');
INSERT INTO authorization_demo.cm_enterprise (id, name, create_time, update_time, del_flag) VALUES (4, '企业4', '2023-03-01 09:33:56', '2023-03-01 09:34:01', '0');
INSERT INTO authorization_demo.cm_enterprise (id, name, create_time, update_time, del_flag) VALUES (5, '企业5', '2023-03-01 09:33:57', '2023-03-01 09:34:02', '0');
```

| id   | name  | create\_time        | update\_time        | del\_flag |
| :--- | :---- | :------------------ | :------------------ | :-------- |
| 1    | 企业1 | 2023-03-01 09:33:48 | 2023-03-01 09:33:58 | 0         |
| 2    | 企业2 | 2023-03-01 09:33:50 | 2023-03-01 09:33:59 | 0         |
| 3    | 企业3 | 2023-03-01 09:33:55 | 2023-03-01 09:34:00 | 0         |
| 4    | 企业4 | 2023-03-01 09:33:56 | 2023-03-01 09:34:01 | 0         |
| 5    | 企业5 | 2023-03-01 09:33:57 | 2023-03-01 09:34:02 | 0         |

##### 用户企业关联表`cm_user_enterprise`

```mysql
-- auto-generated definition
create table cm_user_enterprise
(
    user_id bigint not null comment '用户id',
    ent_id  bigint not null comment '企业id'
)
    comment '用户企业关联表';
INSERT INTO authorization_demo.cm_user_enterprise (user_id, ent_id) VALUES (1, 1);
INSERT INTO authorization_demo.cm_user_enterprise (user_id, ent_id) VALUES (1, 2);
INSERT INTO authorization_demo.cm_user_enterprise (user_id, ent_id) VALUES (1, 3);
INSERT INTO authorization_demo.cm_user_enterprise (user_id, ent_id) VALUES (2, 3);
INSERT INTO authorization_demo.cm_user_enterprise (user_id, ent_id) VALUES (2, 4);
INSERT INTO authorization_demo.cm_user_enterprise (user_id, ent_id) VALUES (2, 5);
```

| user\_id | ent\_id |
| :------- | :------ |
| 1        | 1       |
| 1        | 2       |
| 1        | 3       |
| 2        | 3       |
| 2        | 4       |
| 2        | 5       |

假设用户`admin`具备企业1、2、3的权限。用户`test`具备企业3、4、5的权限

##### 企业数据测试表`cm_ent_data`

```java
create table cm_ent_data
(
    id          bigint auto_increment comment '主键'
        primary key,
    ent_id      bigint       not null comment '企业id',
    description varchar(200) null comment '说明'
)
    comment '用户数据测试的表';
INSERT INTO authorization_demo.cm_ent_data (id, ent_id, description) VALUES (1, 1, '企业1的测试数据1号');
INSERT INTO authorization_demo.cm_ent_data (id, ent_id, description) VALUES (2, 2, '企业2的测试数据1号');
INSERT INTO authorization_demo.cm_ent_data (id, ent_id, description) VALUES (3, 3, '企业3的测试数据1号');
INSERT INTO authorization_demo.cm_ent_data (id, ent_id, description) VALUES (4, 4, '企业4的测试数据1号');
INSERT INTO authorization_demo.cm_ent_data (id, ent_id, description) VALUES (5, 5, '企业5的测试数据1号');
INSERT INTO authorization_demo.cm_ent_data (id, ent_id, description) VALUES (6, 2, '企业2的测试数据2号');
INSERT INTO authorization_demo.cm_ent_data (id, ent_id, description) VALUES (7, 3, '企业3的测试数据2号');

```

| id   | ent\_id | description        |
| :--- | :------ | :----------------- |
| 1    | 1       | 企业1的测试数据1号 |
| 2    | 2       | 企业2的测试数据1号 |
| 3    | 3       | 企业3的测试数据1号 |
| 4    | 4       | 企业4的测试数据1号 |
| 5    | 5       | 企业5的测试数据1号 |
| 6    | 2       | 企业2的测试数据2号 |
| 7    | 3       | 企业3的测试数据2号 |

假设企业2、3分别有2条数据，企业1、4、5分别只有一条数据

#### 这三张表生成对应的controller、service、mapper文件

建议用easycode插件，具体使用方法和模版（生成后的内容需要更新实际情况改改）

{% note green 'fas fa-rocket' %}

 📃 关联文档

{% post_link tools/easycode自定义idea模板 ' 📄 easy code使用及模版' %}

{% endnote %}

![image-20230301094847498](https://mf.allbs.cn/cloudpic/2023/03/97f27c824b17b745358ee22e21accf11.png)

#### 登录时查询用户关联的企业列表并缓存，当然如果怕影响登录速度，可以在登录后执行一个异步方法进行相关操作

具体添加的代码见对应的git提交即可，总之这一步主要目的就是让权限系统中的用户信息中含有该用户的关联企业列表

##### 启动项目后进行测试

##### 未加`dataScope`注解查询企业数据测试表，结果显示查出全部数据

![image-20230301102316946](https://mf.allbs.cn/cloudpic/2023/03/7af1952da271a654e6e9cc2119b73455.png)

##### 添加注解测试，用户admin关联企业1、2、3查询结果符合期望

![image-20230301104139695](https://mf.allbs.cn/cloudpic/2023/03/8189c65b2afd18f887551a81c36da4e0.png)

![image-20230301103938748](https://mf.allbs.cn/cloudpic/2023/03/2c4b94df337feaeb62ce22ad6535f11c.png)

#### 现在有个需求，根据前端需要查询的企业进行数据筛选

##### 增加一个Holder用于储存当前查询ent_id列表

```java
package cn.allbs.allbsjwt.config.datascope;

import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.experimental.UtilityClass;

import java.util.Set;

@UtilityClass
public class CurrentEntIdSearchContextHolder {

    private final ThreadLocal<Set<Long>> THREAD_LOCAL_ENT_LIST = new TransmittableThreadLocal<>();

    /**
     * 设置当前header中的企业列表
     *
     * @param entIdList 需要查询的企业列表
     */
    public void setEntIdList(Set<Long> entIdList) {
        THREAD_LOCAL_ENT_LIST.set(entIdList);
    }

    /**
     * 获取header中的企业列表
     *
     * @return 企业列表
     */
    public Set<Long> getEntIdList() {
        return THREAD_LOCAL_ENT_LIST.get();
    }

    public void clear() {
        THREAD_LOCAL_ENT_LIST.remove();
    }
}

```
##### 添加过滤器，将前端传输的数据储存起来
```java
package cn.allbs.allbsjwt.config.datascope;

import cn.allbs.allbsjwt.config.constant.CommonConstants;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EntIdContextHolderFilter extends GenericFilterBean {

    @Override
    @SneakyThrows
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String entIdListStr = request.getHeader(CommonConstants.ENT_ID_LIST);

        if (StrUtil.isNullOrUndefined(entIdListStr)) {
            CurrentEntIdSearchContextHolder.clear();
        } else {
            Set<Long> entIdList = Convert.toSet(Long.class, entIdListStr);
            CurrentEntIdSearchContextHolder.setEntIdList(entIdList);
            log.debug("获取header中的企业列表为:{}", entIdList);
        }

        filterChain.doFilter(request, response);
        CurrentEntIdSearchContextHolder.clear();
    }
}
```

#### 在`UnitDataPermissionInterceptor`中添加代码过滤用户查询的企业列表

```java
// 获取header中的待过滤的企业列表
Set<Long> entIdList = CurrentEntIdSearchContextHolder.getEntIdList();
if (entIdList != null) {
    if (dataScopeParam == null) {
        dataScopeParam = new DataScopeParam("ent_id", entIdList, true, CollUtil.newArrayList("sys_file"));
    } else {
        // 查询交集
        Set<Long> permissionEntList = dataScopeParam.getEntIdList();
        dataScopeParam.setFilterField(true);
        dataScopeParam.setEntIdList(entIdList.stream().filter(permissionEntList::contains).collect(Collectors.toSet()));
    }
}
```

![image-20230301104910292](https://mf.allbs.cn/cloudpic/2023/03/5b6208d74a985394383c8665687087b8.png)

#### 进行测试

##### 在添加`dataScope`的情况下只查询到企业符合期望

![image-20230301105235512](https://mf.allbs.cn/cloudpic/2023/03/7f3657985029bd5d94171cccae466be3.png)

##### 去掉`datascope`进行测试

没有企业3的数据符合预期

![image-20230301105532643](https://mf.allbs.cn/cloudpic/2023/03/6315663c3904e58284a09e2f2ffe7484.png)

### 实现方式二

暂时没时间，后面再加
