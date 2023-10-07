package com.example.security.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.security.user.Permission.*;
import static com.example.security.user.Role.ADMIN;
import static com.example.security.user.Role.MANAGER;
import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            logger.info("Security Filter Chain trigger");
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers("/api/v1/auth/**")
                    .permitAll()
                    .requestMatchers("/api/v1/management/**").hasAnyRole(MANAGER.name())
                    .requestMatchers(GET,"/api/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
                    .requestMatchers(POST,"/api/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
                    .requestMatchers(PUT,"/api/v1/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
                    .requestMatchers(DELETE,"/api/v1/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())

//                    .requestMatchers("/api/v1/admin/**").hasAnyRole(ADMIN.name(), MANAGER.name())
//                    .requestMatchers(GET,"/api/v1/admin/**").hasAnyAuthority(ADMIN_READ.name(), MANAGER_READ.name())
//                    .requestMatchers(POST,"/api/v1/admin/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGER_CREATE.name())
//                    .requestMatchers(PUT,"/api/v1/admin/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGER_UPDATE.name())
//                    .requestMatchers(DELETE,"/api/v1/admin/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGER_DELETE.name())

                    .anyRequest()
                    .authenticated())
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        }
}
