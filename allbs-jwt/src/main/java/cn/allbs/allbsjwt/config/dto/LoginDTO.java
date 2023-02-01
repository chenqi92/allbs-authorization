package cn.allbs.allbsjwt.config.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 类 LoginDTO
 * </p>
 * 用户登录信息
 *
 * @author ChenQi
 * @since 2023/2/1 16:02
 */
@Data
@Schema(title = "用户登录信息", name = "LoginDTO")
public class LoginDTO {

    /**
     * 用户名
     */
    @Schema(description = "用户名", name = "username", implementation = String.class, example = "张三")
    private String username;

    /**
     * 密码
     */
    @Schema(description = "加密后密码", name = "password", implementation = String.class)
    private String password;
}
