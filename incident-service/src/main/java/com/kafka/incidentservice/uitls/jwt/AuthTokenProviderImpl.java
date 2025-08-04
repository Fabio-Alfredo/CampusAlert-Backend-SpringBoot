package com.kafka.incidentservice.uitls.jwt;

import com.kafka.incidentservice.domain.dtos.UserDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class AuthTokenProviderImpl implements IAuthProvider{

    @Value("${jwt.security.auth.secret-key}")
    private String secretKey;

    @Override
    public boolean isValidToken(String token) {
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build();
            var claims = parser.parseSignedClaims(token).getPayload();
            String type = claims.get("typ", String.class);
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

            Claims claims = parser.parseSignedClaims(token).getPayload();
            UserDto user = new UserDto();
            user.setId(UUID.fromString(claims.get("id", String.class)));
            user.setEmail(claims.getSubject());

            List<String> roles = ((List<?>) claims.get("roles"))
                    .stream()
                    .map(Object::toString)
                    .toList();
            user.setRoles(roles);

            return user;
        }catch (Exception e){
            return  null;
        }
    }
}
