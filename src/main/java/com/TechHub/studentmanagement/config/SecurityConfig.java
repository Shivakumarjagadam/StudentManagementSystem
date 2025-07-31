package com.TechHub.studentmanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.Customizer;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for Postman testing
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // Allow register & login
                .requestMatchers(HttpMethod.DELETE, "/courses/**").hasAuthority("TEACHER") // Only teacher can delete
                .anyRequest().authenticated() // Others require login
            )
            .httpBasic(Customizer.withDefaults()); // ✅ Modern way to enable basic auth

        return http.build();
    }
}
