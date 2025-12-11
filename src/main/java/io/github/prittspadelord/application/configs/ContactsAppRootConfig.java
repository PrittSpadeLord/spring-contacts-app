package io.github.prittspadelord.application.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = {
    "io.github.prittspadelord.application.rest.support",
    "io.github.prittspadelord.application.rest.interceptors",
    "io.github.prittspadelord.application.services.impl"
})
@Configuration
public class ContactsAppRootConfig {}