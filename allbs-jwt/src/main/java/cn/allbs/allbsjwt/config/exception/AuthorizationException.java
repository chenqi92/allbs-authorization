package cn.allbs.allbsjwt.config.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;

/**
 * 类 AuthorizationException
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/1 17:35
 */
@JsonSerialize(using = AuthorizationExceptionSerializer.class)
public class AuthorizationException extends RuntimeException {

    @Getter
    private String errorCode;

    public AuthorizationException(String msg) {
        super(msg);
    }

    public AuthorizationException(String msg, Throwable t) {
        super(msg, t);
    }

    public AuthorizationException(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}
