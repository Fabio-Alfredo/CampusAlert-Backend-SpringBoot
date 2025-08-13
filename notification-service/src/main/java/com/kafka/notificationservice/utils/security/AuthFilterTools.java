package com.kafka.notificationservice.utils.security;

import com.kafka.notificationservice.domain.dtos.UserDto;
import com.kafka.notificationservice.utils.jwt.contract.IAuthProvider;
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
public class AuthFilterTools extends OncePerRequestFilter {

    private final IAuthProvider authProvider;

    public AuthFilterTools(IAuthProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        String token = null;
        boolean isValid=false;

        if(tokenHeader != null && tokenHeader.startsWith("Bearer ") && tokenHeader.length() >7){
            token = tokenHeader.substring(7);
            try{
                isValid = authProvider.isValidToken(token);
            }catch (IllegalArgumentException e){
                System.out.println("Invalid token format: " + e.getMessage());
            }catch (MalformedJwtException e){
                System.out.println("Malformed JWT token: " + e.getMessage());
            }catch (ExpiredJwtException e){
                System.out.println("Expired JWT token: " + e.getMessage());
            }
        }else{
            System.out.println("Authorization header is missing or invalid");
        }

        if(token !=null && isValid && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDto user = authProvider.getUserFromToken(token);
            if(user !=null){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user, token, null);

                authenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }else{
                System.out.println("User not found in token");
            }
        }else{
            System.out.println("Token is invalid or user is already authenticated");
        }

        filterChain.doFilter(request, response);
    }
}
