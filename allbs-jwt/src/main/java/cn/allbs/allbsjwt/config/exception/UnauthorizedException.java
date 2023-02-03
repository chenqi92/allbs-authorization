package cn.allbs.allbsjwt.config.exception;

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
        super(msg);
    }

    public UnauthorizedException(String msg) {
        super(msg);
    }
}
