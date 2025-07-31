    package com.kafka.auditservice.configurations.auth;

    import com.kafka.auditservice.utils.security.AuthFiltersTools;
    import jakarta.servlet.http.HttpServletResponse;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

    import static org.springframework.security.config.Customizer.withDefaults;

    @Configuration
    @EnableWebSecurity
    @EnableMethodSecurity
    public class WebSecurityConfiguration {

        private final AuthFiltersTools authFiltersTools;

        public WebSecurityConfiguration(AuthFiltersTools authFiltersTools) {
            this.authFiltersTools = authFiltersTools;
        }

        @Bean
        SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
            http.csrf(csrf -> csrf.disable());


            http.authorizeHttpRequests(authorize ->
                authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .anyRequest().authenticated()
            );

            http.sessionManagement(management-> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

            http.exceptionHandling(handling ->handling.authenticationEntryPoint((req, res, ex)->{
                res.sendError(
                        HttpServletResponse.SC_UNAUTHORIZED,
                        "Unauthorized: " + ex.getMessage()
                );
            }));

            http.addFilterBefore(authFiltersTools, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        }
    }
