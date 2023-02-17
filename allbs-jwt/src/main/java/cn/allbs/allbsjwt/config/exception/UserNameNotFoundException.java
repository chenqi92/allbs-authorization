package cn.allbs.allbsjwt.config.exception;

import cn.allbs.allbsjwt.config.enums.SystemCode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * 类 UserNameNotFoundException
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/16 15:56
 */
@JsonSerialize(using = AuthorizationExceptionSerializer.class)
public class UserNameNotFoundException extends AuthorizationException {

    public UserNameNotFoundException(String msg, Throwable t) {
        super(msg, SystemCode.USER_NOT_FOUND_ERROR.getCode());
    }

    public UserNameNotFoundException(String msg) {
        super(msg, SystemCode.USER_NOT_FOUND_ERROR.getCode());
    }
}
