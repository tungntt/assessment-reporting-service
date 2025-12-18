package com.xphr.reporting.ms.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()  // Explicitly allow GET /login
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()  // Static resources
                        .dispatcherTypeMatchers(jakarta.servlet.DispatcherType.FORWARD).permitAll()  // Allow internal forwards
                        .requestMatchers("/report/**").hasAnyRole("EMPLOYEE", "ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(authenticationSuccessHandler())
                        .failureHandler(authenticationFailureHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                );

        return http.build();
    }


    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails tom = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password"))
                .roles("EMPLOYEE")
                .build();

        UserDetails jerry = User.builder()
                .username("jerry")
                .password(passwordEncoder.encode("password"))
                .roles("EMPLOYEE")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin123"))
                .roles("ADMIN")
                .build();

        log.info("Configured in-memory users: tom (EMPLOYEE), jerry (EMPLOYEE), admin (ADMIN)");

        return new InMemoryUserDetailsManager(tom, jerry, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            log.info("User '{}' successfully authenticated with roles: {}",
                    authentication.getName(),
                    authentication.getAuthorities());
            response.sendRedirect("/report/time-record");
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            log.warn("Authentication failed: {}", exception.getMessage());
            response.sendRedirect("/login?error");
        };
    }
}
