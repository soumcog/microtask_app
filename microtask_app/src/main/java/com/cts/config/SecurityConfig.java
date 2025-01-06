package com.cts.config;

import com.cts.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true) // Enable method-level security
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
                .csrf(csrf -> csrf
                        .disable())
                .authorizeHttpRequests(requests -> requests
                        // Public endpoints (no authentication required)
                        .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // Allow registration and login for all

                        // Job endpoints
                        .requestMatchers(HttpMethod.GET, "/api/jobs/**").hasAnyRole("ADMIN", "EMPLOYER", "WORKER") // Only ADMIN, EMPLOYER, and WORKER can view jobs
                        .requestMatchers(HttpMethod.POST, "/api/jobs/**").hasRole("EMPLOYER") // Only employers can post jobs
                        .requestMatchers(HttpMethod.PUT, "/api/jobs/**").hasRole("EMPLOYER") // Only employers can edit jobs
                        .requestMatchers(HttpMethod.DELETE, "/api/jobs/**").hasRole("EMPLOYER") // Only employers can delete jobs

                        // Admin endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Only admins can access admin endpoints

                        // All other requests need to be authenticated
                        .anyRequest().authenticated())
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}