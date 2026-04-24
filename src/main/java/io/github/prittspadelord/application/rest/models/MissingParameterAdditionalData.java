package io.github.prittspadelord.application.rest.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MissingParameterAdditionalData {
    private String parameterName;
    private String parameterType;
}