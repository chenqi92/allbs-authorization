package cn.allbs.allbsjwt.config.enums;

import cn.allbs.common.code.IResultCode;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 枚举 SystemCode
 * </p>
 * 自定义异常code及提示信息
 *
 * @author ChenQi
 * @since 2023/2/1 15:12
 */
@Getter
@RequiredArgsConstructor
@ApiModel(description = "自定义异常code")
public enum SystemCode implements IResultCode {

    /**
     * 自定义异常code枚举
     */
    FORBIDDEN_401(401, "未经认证!"),

    TOKEN_NOT_IN_SYSTEM(401001, "token失效!"),

    AUTHORIZATION_ERROR(401002, "权限处理逻辑出现异常!"),

    USERNAME_OR_PASSWORD_ERROR(401003, "用户名或密码错误"),

    USER_NOT_FOUND_ERROR(401004, "不存在的用户名"),

    FORBIDDEN_403(403, "缺少资源访问权限!"),
    ;

    /**
     * code编码
     */
    private final int code;
    /**
     * 中文信息描述
     */
    private final String msg;
}
