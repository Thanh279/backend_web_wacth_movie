package com.test.demo.config;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.util.Base64;
import com.test.demo.service.TokenService;

@Configuration
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Value("${pcv.jwt.base64-secret}")
    private String jwtKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http, CustomAuthenticationEntryPoint customAuthenticationEntryPoint)
            throws Exception {
        String[] whiteList = {
                "/",
                "/api/animations",
                "/api/animations/**",
                "/api/episodes",
                "/api/episodes/**",
                "/api/banners",
                "/api/genres",
                "/api/genres/**",
                "/api/categories",
                "/api/v1/users/**",
                "/api/v1/auth/login",
                "/api/v1/auth/refresh",
                // "/api/v1/auth/**",
                // "/login/oauth2/code/google",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/phim/**",
                // "api/v1/auth/google/success",
                "/api/series",
                "/api/series/**",
                "api/content",
                "api/content/**"
        };

        http
                .csrf(c -> c.disable())
                .cors(Customizer.withDefaults())

                .authorizeHttpRequests(
                        authz -> authz

                                .requestMatchers(whiteList).permitAll()

                                .anyRequest().authenticated())

                .oauth2Login(oauth2 -> oauth2
                        // .loginPage("/api/v1/auth/google")
                        // .defaultSuccessUrl("/api/v1/auth/google/success", true)
                        // .failureUrl("/api/v1/auth/google/failure")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userAuthoritiesMapper(authorities -> authorities)))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(Customizer.withDefaults())
                        .authenticationEntryPoint(customAuthenticationEntryPoint))
                .formLogin(f -> f.disable())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withSecretKey(
                getSecretKey()).macAlgorithm(TokenService.JWT_ALGORITHM).build();
        return token -> {
            try {
                return jwtDecoder.decode(token);
            } catch (Exception e) {
                System.out.println(">>> JWT error: " + e.getMessage());
                throw e;
            }
        };
    }

    @Bean
    public JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(getSecretKey()));
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.from(jwtKey).decode();
        return new SecretKeySpec(keyBytes, 0, keyBytes.length,
                TokenService.JWT_ALGORITHM.getName());
    }

}