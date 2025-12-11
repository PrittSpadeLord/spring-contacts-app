package io.github.prittspadelord.application.configs;

import io.github.prittspadelord.application.rest.interceptors.RateLimitingInterceptor;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;
import tools.jackson.databind.json.JsonMapper;

@AllArgsConstructor
@ComponentScan(basePackages = "io.github.prittspadelord.application.rest.controllers")
@Configuration
@EnableWebMvc
public class ContactsAppWebConfig implements WebMvcConfigurer {

    private final RateLimitingInterceptor rateLimitingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(rateLimitingInterceptor);
    }

    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder converters) {
        JsonMapper mapper = JsonMapper.builder()
            .enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
            .enable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            .enable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
            .enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

            .disable(DateTimeFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)
            .disable(DateTimeFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .build();

        converters.addCustomConverter(new JacksonJsonHttpMessageConverter(mapper));
    }
}