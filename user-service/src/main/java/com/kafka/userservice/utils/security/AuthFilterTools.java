package com.kafka.userservice.utils.token.security;

import com.kafka.userservice.domain.enums.TypeToken;
import com.kafka.userservice.utils.token.factory.TokenProviderFactory;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilterTools extends OncePerRequestFilter {

    private final TokenProviderFactory tokenProviderFactory;

    public AuthFilterTools(TokenProviderFactory tokenProviderFactory) {
        this.tokenProviderFactory = tokenProviderFactory;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        String token = null;
        String email = null;
        if(tokenHeader != null && tokenHeader.startsWith("Bearer ") && tokenHeader.length() >7){
            token = tokenHeader.substring(7);
            try{
                email = tokenProviderFactory.of(TypeToken.AUTH_TOKEN).getEmailFromToken(token);
            }catch (IllegalArgumentException e){
                System.out.println("No se puede obtener el token");
            }catch (MalformedJwtException e){
                System.out.println("Token invalido para autenticacion");
            }catch (ExpiredJwtException e){
                System.out.println("El token ya ha expirado");
            }
        }else{
            System.out.println("Bearer String not found");
        }
        if(token!= null && email !=null  && SecurityContextHolder.getContext().getAuthentication() == null){
            
        }
    }
}
