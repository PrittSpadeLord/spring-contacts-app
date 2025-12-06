package io.github.prittspadelord.application.configs;

import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan(basePackages = {
    "io.github.prittspadelord.application.factories",
    "io.github.prittspadelord.application.rest.interceptors"
})
@Configuration
@Slf4j
public class ContactsAppRootConfig {
}
