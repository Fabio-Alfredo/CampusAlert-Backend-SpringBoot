package com.kafka.auditservice.utils.token;

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
import java.util.stream.Collectors;

@Component
public class AuthTokenProvider implements ITokenProvider {
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
        } catch (Exception e) {
            return null;
        }
    }
}
