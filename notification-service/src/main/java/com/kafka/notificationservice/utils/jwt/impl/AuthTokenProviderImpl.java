package com.kafka.notificationservice.utils.jwt.impl;

import com.kafka.notificationservice.domain.dtos.UserDto;
import com.kafka.notificationservice.utils.jwt.contract.IAuthProvider;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AuthTokenProviderImpl implements IAuthProvider {

    @Value("${jwt.security.auth.secret-key}")
    private String secretKey;

    @Override
    public boolean isValidToken(String token) {
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

    @Override
    public UserDto getUserFromToken(String token) {
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build();

            var claims = parser.parseSignedClaims(token).getPayload();
            UserDto user = new UserDto();

            user.setId(claims.get("id", UUID.class));
            user.setEmail(claims.getSubject());
            user.setRoles(((List<?>) claims.get("roles"))
                    .stream()
                    .map(Object::toString)
                    .toList());

            return user;
        }catch (Exception e){
            return null;
        }
    }
}

