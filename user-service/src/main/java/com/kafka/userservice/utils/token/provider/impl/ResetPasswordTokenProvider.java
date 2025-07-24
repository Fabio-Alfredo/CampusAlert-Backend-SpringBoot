package com.kafka.userservice.utils.token.provider.impl;

import com.kafka.userservice.domain.enums.TypeToken;
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
import java.util.Objects;

@Component
public class ResetPasswordTokenProvider implements ITokenProvider {

    @Value("${jwt.reset.password.expiration}")
    private Long expiration;
    @Value("${jwt.reset.password.secret-key}")
    private String secretKey;


    @Override
    public TypeToken supportedTypeToken() {
        return TypeToken.RESET_PASSWORD_TOKEN;
    }

    @Override
    public String generateToken(User user) {
        Map<String, Object>claims = new HashMap<>();
        claims.put("type", TypeToken.RESET_PASSWORD_TOKEN);

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
    public boolean validateToken(String token, User user) {
        try{
            JwtParser parser = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build();

            parser.parse(token);

            return true;
        }catch (Exception e ){
            return false;
        }
    }
}
