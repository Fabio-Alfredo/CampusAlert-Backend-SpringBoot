package com.kafka.notificationservice.configurations.security;

import com.kafka.notificationservice.utils.security.AuthFilterTools;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityWebConfiguration {

    private final AuthFilterTools authFilterTools;

    public SecurityWebConfiguration(AuthFilterTools authFilterTools) {
        this.authFilterTools = authFilterTools;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
        http.csrf(csrf-> csrf.disable());

        http.authorizeHttpRequests(auth->
                auth.requestMatchers("/api/notifications/**").permitAll()
                        .anyRequest().authenticated()
        );

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(exception ->
                exception.authenticationEntryPoint((req, res, ex)->{
                    res.sendError(
                            401,
                            "Unauthorized: " + ex.getMessage()
                    );
                })
        );

        http.addFilterBefore(authFilterTools, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
