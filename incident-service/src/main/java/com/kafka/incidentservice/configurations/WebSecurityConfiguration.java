package com.kafka.incidentservice.configurations;

import com.kafka.incidentservice.uitls.security.AuthFilterTools;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfiguration {

    private final AuthFilterTools authFilterTools;

    public WebSecurityConfiguration(AuthFilterTools authFilterTools) {
        this.authFilterTools = authFilterTools;
    }

    @Bean
    SecurityFilterChain securityFilterChain (HttpSecurity security)throws Exception{
        security.csrf(csrf -> csrf.disable());

        security.authorizeHttpRequests(auth ->
                auth.requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
        );

        security.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        security.exceptionHandling(exception->
                        exception.authenticationEntryPoint((req, res, ex)->{
                            res.sendError(
                                    HttpServletResponse.SC_UNAUTHORIZED,
                                    "Unauthorized: " + ex.getMessage()
                            );
                        })
        );

        security.addFilterBefore(authFilterTools, UsernamePasswordAuthenticationFilter.class);
        return security.build();
    }

}
