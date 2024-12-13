package com.Project.AppApplication.infra.security.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods(HttpMethod.POST.name(),
                                HttpMethod.GET.name(),
                                HttpMethod.PUT.name(),
                                HttpMethod.DELETE.name(),
                                HttpMethod.OPTIONS.name(),
                                HttpMethod.PATCH.name(),
                                HttpMethod.TRACE.name())
                        .allowedHeaders(HttpHeaders.CONTENT_TYPE,HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                                HttpHeaders.AUTHORIZATION);
            }
        };
    }
}
