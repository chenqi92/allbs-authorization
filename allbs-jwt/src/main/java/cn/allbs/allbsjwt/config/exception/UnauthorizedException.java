package cn.allbs.allbsjwt.config.exception;

import cn.allbs.allbsjwt.config.enums.SystemCode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 类 UnauthorizedException
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/3 15:41
 */
@JsonSerialize(using = AuthorizationExceptionSerializer.class)
public class UnauthorizedException extends AuthorizationException {

    public UnauthorizedException(String msg, Throwable t) {
        super(msg, SystemCode.TOKEN_NOT_IN_SYSTEM.getCode());
    }

    public UnauthorizedException(String msg) {
        super(msg, SystemCode.TOKEN_NOT_IN_SYSTEM.getCode());
    }
}
