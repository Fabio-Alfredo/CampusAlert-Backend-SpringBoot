package com.kafka.userservice.utils.token.provider.impl;

import com.kafka.userservice.domain.enums.TypeToken;
import com.kafka.userservice.domain.models.Role;
import com.kafka.userservice.domain.models.User;
import com.kafka.userservice.utils.token.provider.contract.ITokenProvider;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthTokenProvider implements ITokenProvider {

    @Value("${jwt.security.auth.secret-key}")
    private String secretKey;
    @Value("${jwt.security.auth.expiration}")
    private Long expiration;

    @Override
    public TypeToken supportedTypeToken() {
        return TypeToken.AUTH_TOKEN;
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", TypeToken.AUTH_TOKEN);
        claims.put("id", user.getId());
        claims.put("roles", user.getRoles().stream().map(Role::getId).toList());

        return Jwts.builder()
                .claims(claims)
                .subject(user.getEmail())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+expiration*1000L))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    @Override
    public String getEmailFromToken(String token) {
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build();

            return parser.parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public boolean validateToken(String token) {
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build();

            parser.parse(token);

            return true;
        }catch (Exception e){
            return false;
        }
    }
}
