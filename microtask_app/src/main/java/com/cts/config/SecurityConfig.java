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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter; //custom filter for jwt based authentication, get the jwt from auth header and validates it 

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationProvider authenticationProvider) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF protection
            .authorizeHttpRequests(requests -> requests
            	//access rules
                // Public endpoints
                .requestMatchers(HttpMethod.POST, "/api/auth/**").permitAll() // Allow registration and login
                
                // Profile module endpoints (accessible to authenticated users)
                .requestMatchers("/api/profile/**").authenticated()

                // Job module endpoints
                .requestMatchers(HttpMethod.GET, "/api/jobs/**").hasAnyRole("ADMIN", "EMPLOYER", "WORKER") // Allow all roles to view jobs
                .requestMatchers(HttpMethod.POST, "/api/jobs/**").hasRole("EMPLOYER") // Only employers can create jobs
                .requestMatchers(HttpMethod.PUT, "/api/jobs/**").hasRole("EMPLOYER") // Only employers can update jobs
                .requestMatchers(HttpMethod.DELETE, "/api/jobs/**").hasRole("EMPLOYER") // Only employers can delete jobs

                // Admin module endpoints
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Only admins can access admin endpoints

                // All other requests must be authenticated
                .anyRequest().authenticated()
            )
            .sessionManagement(management -> management
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Stateless session management
            )
            .authenticationProvider(authenticationProvider) // Use custom authentication provider
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add JWT filter

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt for password encoding
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOriginPattern("*"); // Allow requests from any origin (for development, i will change later
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config); // Apply this config to all paths
        return new CorsFilter(source);
    }
}