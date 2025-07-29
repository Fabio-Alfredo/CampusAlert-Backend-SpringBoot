package com.kafka.auditservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kafka.auditservice.domain.dtos.auth.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class JwtTools {
    @Value("${jwt.security.auth.secret-key}")
    private String secretKey;

    public boolean isValidToken(String token){
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build();
            var claims = parser.parseSignedClaims(token).getPayload();
            String type = claims.get("type", String.class);

            return "AUTH_TOKEN".equals(type);
        }catch (Exception e){
            return false;
        }
    }

    public UserDto getUserFromToken(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build();

            Jws<Claims> jwsClaims = parser.parseSignedClaims(token);
            Claims claims = jwsClaims.getPayload();

            String email = claims.getSubject();
            List<String> roles = claims.get("roles", List.class);
            UUID userId = claims.get("id", UUID.class);

            UserDto userDto = new UserDto();
            userDto.setEmail(email);
            userDto.setRoles(roles);
             userDto.setId(userId);

            return userDto;
        } catch (Exception e) {
            return null;
        }
    }
}
