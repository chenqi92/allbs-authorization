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
