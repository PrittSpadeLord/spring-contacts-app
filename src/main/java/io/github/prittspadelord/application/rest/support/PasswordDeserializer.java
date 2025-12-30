package io.github.prittspadelord.application.rest.support;

import org.bouncycastle.util.Arrays;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.ValueDeserializer;

public class PasswordDeserializer extends ValueDeserializer<char[]> {

    @Override
    public char[] deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        if (p.currentToken() == JsonToken.VALUE_NULL) return null;

        char[] sourceBuffer = p.getStringCharacters();
        int offset = p.getStringOffset();
        int length = p.getStringLength();

        //Make sure to add exception handling if the DeserializationContext type isnt char[]!

        return Arrays.copyOfRange(sourceBuffer, offset, offset + length);
    }
}