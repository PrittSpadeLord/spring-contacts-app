package io.github.prittspadelord.application.rest.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiErrorInfo {
    private int status;
    private String timestamp;
    private String errorType;
    private String description;
}
