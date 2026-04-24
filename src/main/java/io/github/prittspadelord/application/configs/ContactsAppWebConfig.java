package io.github.prittspadelord.application.configs;

import io.github.prittspadelord.application.rest.interceptors.AuthorizationInterceptor;
import io.github.prittspadelord.application.rest.interceptors.RateLimitingInterceptor;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverters;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import tools.jackson.databind.json.JsonMapper;

@ComponentScan(basePackages = {
    "io.github.prittspadelord.application.rest.controllers.v1",
    "io.github.prittspadelord.application.rest.controllers.v2"
})
@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class ContactsAppWebConfig implements WebMvcConfigurer {

    private final JsonMapper jsonMapper;

    private final AuthorizationInterceptor authorizationInterceptor;
    private final RateLimitingInterceptor rateLimitingInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .allowedOrigins(System.getenv("BASE_URL"))
            .allowedMethods(
                HttpMethod.GET.toString(),
                HttpMethod.POST.toString(),
                HttpMethod.PUT.toString(),
                HttpMethod.PATCH.toString(),
                HttpMethod.DELETE.toString()
            )
            .allowedHeaders("*")
            .exposedHeaders("Authorization")
            .allowCredentials(false);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.rateLimitingInterceptor);
        registry.addInterceptor(this.authorizationInterceptor);
    }

    @Override
    public void configureMessageConverters(HttpMessageConverters.ServerBuilder converters) {
        converters.addCustomConverter(new JacksonJsonHttpMessageConverter(this.jsonMapper));
    }
}