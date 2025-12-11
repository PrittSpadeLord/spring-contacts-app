package io.github.prittspadelord.application.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class ContactsAppSecurityConfig {

    @Bean
    Argon2PasswordEncoder passwordEncoder() {
        return Argon2PasswordEncoder.defaultsForSpringSecurity_v5_8();
    }

//    @Bean
//    JwtDecoder jwtDecoder() {
//        String secret = ""; //where will this come from?
//
//        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//
//        return NimbusJwtDecoder.withSecretKey(secretKey).build();
//    }
//
//    @Bean
//    JwtEncoder jwtEncoder() {
//        String secret = ""; //where will this come from?
//
//        SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
//
//        return NimbusJwtEncoder.withSecretKey(secretKey).build();
//    }
}
