package cn.allbs.allbsjwt.config.exception;

import cn.allbs.allbsjwt.config.enums.SystemCode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * 类 AuthorizationExceptionSerializer
 * </p>
 *
 * @author ChenQi
 * @since 2023/2/1 17:34
 */
public class AuthorizationExceptionSerializer extends StdSerializer<AuthorizationException> {

    public AuthorizationExceptionSerializer() {
        super(AuthorizationException.class);
    }

    @Override
    public void serialize(AuthorizationException value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("code", SystemCode.AUTHORIZATION_ERROR.getCode());
        gen.writeStringField("msg", value.getMessage());
        gen.writeStringField("data", value.getErrorCode());
        gen.writeEndObject();
    }
}
