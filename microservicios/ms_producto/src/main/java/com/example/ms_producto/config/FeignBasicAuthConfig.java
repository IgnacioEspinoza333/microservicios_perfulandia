package com.example.ms_producto.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class FeignBasicAuthConfig {

    @Bean
    public RequestInterceptor basicAuthRequestInterceptor(
            @Value("${integraciones.auth.username}") String username,
            @Value("${integraciones.auth.password}") String password
    ) {
        return requestTemplate -> {
            String auth = username + ":" + password;
            String encoded = Base64.getEncoder()
                    .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            requestTemplate.header(HttpHeaders.AUTHORIZATION, "Basic " + encoded);
            requestTemplate.header(HttpHeaders.ACCEPT, MediaTypes.HAL_JSON_VALUE);
        };
    }
}