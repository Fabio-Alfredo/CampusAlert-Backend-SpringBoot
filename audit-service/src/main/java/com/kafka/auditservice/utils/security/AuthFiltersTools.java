package com.kafka.auditservice.utils.security;

import com.kafka.auditservice.domain.dtos.auth.UserDto;
import com.kafka.auditservice.utils.token.ITokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFiltersTools extends OncePerRequestFilter {

    private final ITokenProvider iTokenProvider;

    public AuthFiltersTools(ITokenProvider iTokenProvider) {
        this.iTokenProvider = iTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        String token = null;
        boolean isValid = false;
        if(tokenHeader !=null && tokenHeader.startsWith("Bearer ") && tokenHeader.length()>7){
            token = tokenHeader.substring(7);
            try{
                isValid = iTokenProvider.isValidToken(token);
            }catch (IllegalArgumentException e){
               System.out.println("Invalid token format: " + e.getMessage());
            }catch (MalformedJwtException e){
                System.out.println("Malformed token: " + e.getMessage());
            }catch (ExpiredJwtException e){
                System.out.println("Token expired: " + e.getMessage());
            }
        }

        if(token != null &&  isValid && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDto userDto = iTokenProvider.getUserFromToken(token);
            if(userDto != null){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDto, null, userDto.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
