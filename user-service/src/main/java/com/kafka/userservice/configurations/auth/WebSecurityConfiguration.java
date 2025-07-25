package com.kafka.userservice.configurations.auth;

import com.kafka.userservice.services.contract.UserService;
import com.kafka.userservice.utils.security.AuthFilterTools;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthFilterTools authFilterTools;

    public WebSecurityConfiguration(UserService userService, PasswordEncoder passwordEncoder, AuthFilterTools authFilterTools) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authFilterTools = authFilterTools;
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http)throws Exception{
        AuthenticationManagerBuilder managerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        managerBuilder.userDetailsService(identifier-> {
            var user = userService.findByIdentifier(identifier);
            if(user == null)
                throw new UsernameNotFoundException("Usuario no encontrado");
            return user;
        }).passwordEncoder(passwordEncoder);

        return managerBuilder.build();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.httpBasic(withDefaults()).csrf(csrf->csrf.disable());

        http.authorizeHttpRequests(auth->
                auth.requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
        );

        http.sessionManagement(management->management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(handling-> handling.authenticationEntryPoint((req, res ,ex)->{
            res.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Error de autenticacion: " + ex.getMessage()
            );
        }));

        http.addFilterBefore(authFilterTools, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
