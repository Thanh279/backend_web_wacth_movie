package com.test.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/", "/api/v1/auth/**", "/api/v1/email", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
                "/api/v1/watch-history", "/api/v1/favorites", "/api/v1/favorites/**", "/api/series/{seriesId}/comments",
                "/api/v1/vip-packages",
                "/api/v1/users/*/wallet/deposit", "/api/v1/users/*/wallet"

        };

        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);
    }
}