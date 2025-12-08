package io.github.prittspadelord.application.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = {
    "io.github.prittspadelord.application.factories",
    "io.github.prittspadelord.application.rest.interceptors"
})
@Configuration

public class ContactsAppRootConfig {
}
