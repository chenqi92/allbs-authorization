package cn.allbs.allbsjwt.config.exception;

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
        gen.writeObjectField("code", value.getErrorCode());
        gen.writeStringField("msg", value.getMessage());
        gen.writeStringField("data", null);
        gen.writeEndObject();
    }
}
