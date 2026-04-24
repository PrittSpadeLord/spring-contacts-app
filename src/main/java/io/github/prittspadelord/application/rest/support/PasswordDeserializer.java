package io.github.prittspadelord.application.rest.support;

import org.bouncycastle.util.Arrays;

import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

public class PasswordDeserializer extends ValueDeserializer<char[]> {

    @Override
    public ValueDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        if(property == null) return this;

        Class<?> rawClass = property.getType().getRawClass();

        if (rawClass != char[].class) throw new RuntimeException("Fields has PasswordDeserializer annotated despite not being char[] but instead a " + ctxt.getContextualType().getRawClass().getName());

        return null;
    }

    @Override
    public char[] deserialize(JsonParser p, DeserializationContext ctxt) {
        if (p.currentToken() == JsonToken.VALUE_NULL) return null;

        char[] sourceBuffer = p.getStringCharacters();
        int offset = p.getStringOffset();
        int length = p.getStringLength();

        if(!ctxt.getContextualType().hasRawClass(char[].class)) throw new RuntimeException("Fields has PasswordDeserializer annotated despite not being char[] but instead a " + ctxt.getContextualType().getRawClass().getName());

        return Arrays.copyOfRange(sourceBuffer, offset, offset + length);
    }
}