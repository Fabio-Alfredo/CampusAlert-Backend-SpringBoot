package com.kafka.userservice.services.impl;

import com.kafka.userservice.domain.enums.TypeToken;
import com.kafka.userservice.domain.models.Token;
import com.kafka.userservice.domain.models.User;
import com.kafka.userservice.repositories.TokenRepository;
import com.kafka.userservice.services.contract.TokenService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    public TokenServiceImpl(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public boolean isValidToken(User user, TypeToken type, String token) {
        try {
            cleanTokens(type, user);
            List<Token> tokens = tokenRepository.findByTypeTokenAndUserAndCanActive(type, user, true);
            tokens.stream()
                    .map(t-> t.getToken().equals(token))
                    .findAny()
                    .orElseThrow(()-> new Exception());

            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public void cleanTokens(TypeToken type, User user) {

    }
}
