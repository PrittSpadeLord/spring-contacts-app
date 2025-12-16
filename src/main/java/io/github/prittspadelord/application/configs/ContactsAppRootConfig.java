package io.github.prittspadelord.application.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = {
    "io.github.prittspadelord.application.components",
    "io.github.prittspadelord.application.rest.interceptors",
    "io.github.prittspadelord.application.services",
})
@Configuration
public class ContactsAppRootConfig {}