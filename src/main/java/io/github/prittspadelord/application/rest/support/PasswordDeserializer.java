package io.github.prittspadelord.application.rest.support;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.bouncycastle.util.Arrays;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.ValueDeserializer;

@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class PasswordDeserializer extends ValueDeserializer<Object> {

    private final JavaType targetType;

    @Override
    public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        JavaType type = (property != null) ? property.getType() : ctxt.getContextualType();
        return new PasswordDeserializer(type);
    }

    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        if (p.currentToken() == JsonToken.VALUE_NULL) return null;

        char[] sourceBuffer = p.getStringCharacters();
        int offset = p.getStringOffset();
        int length = p.getStringLength();

        if(targetType != null && targetType.getRawClass() == char[].class) {
            return Arrays.copyOfRange(sourceBuffer, offset, offset + length);
        }

        return p.getString();
    }
}