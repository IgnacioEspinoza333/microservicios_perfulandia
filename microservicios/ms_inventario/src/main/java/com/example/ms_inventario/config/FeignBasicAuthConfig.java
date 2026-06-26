package com.example.ms_inventario.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class FeignBasicAuthConfig {

    @Value("${integraciones.auth.username:user}")
    private String username;

    @Value("${integraciones.auth.password:1234}")
    private String password;

    @Bean
    public RequestInterceptor basicAuthRequestInterceptor() {
        return requestTemplate -> {
            String auth = username + ":" + password;
            String encoded = Base64.getEncoder()
                    .encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            requestTemplate.header(HttpHeaders.AUTHORIZATION, "Basic " + encoded);
            requestTemplate.header(HttpHeaders.ACCEPT, "application/json");
        };
    }
}