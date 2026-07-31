package com.example.main.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;


    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }


    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173")
        );

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                )
        );

        configuration.setAllowedHeaders(
                List.of("*")
        );

        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration(
                "/**",
                configuration
        );


        return source;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {


        http

                // Disable CSRF for REST API
                .csrf(
                        csrf -> csrf.disable()
                )


                // Enable CORS
                .cors(
                        cors -> cors.configurationSource(
                                corsConfigurationSource()
                        )
                )


                // Authorization rules
                .authorizeHttpRequests(
                        auth -> auth


                                // Public APIs

                                .requestMatchers(
                                        "/api/auth/login",
                                        "/api/auth/signup",
                                        "/api/auth/refresh"
                                )
                                .permitAll()



                                // Admin only

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/student"
                                )
                                .hasRole("ADMIN")

                                // Profile APIs
                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/student/profile"
                                )
                                .hasAnyRole("ADMIN", "USER")


                                .requestMatchers(
                                        HttpMethod.PUT,
                                        "/student/**"
                                )
                                .hasRole("ADMIN")


                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/student/**"
                                )
                                .hasRole("ADMIN")


                                // Admin + User

                                .requestMatchers(
                                        "/student/profile"
                                )
                                .hasAnyRole(
                                        "ADMIN",
                                        "USER"
                                )


                                // All other APIs require login

                                .anyRequest()
                                .authenticated()
                )


                // JWT is stateless

                .sessionManagement(
                        session ->
                                session.sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS
                                )
                )


                // JWT Filter

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }
}