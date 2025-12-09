package io.github.prittspadelord.application.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@ComponentScan(basePackages = {
    "io.github.prittspadelord.application.factories",
    "io.github.prittspadelord.application.rest.interceptors",
    "io.github.prittspadelord.application.services.impl"
})
@Configuration
public class ContactsAppRootConfig {

    @Bean
    Argon2PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }
}