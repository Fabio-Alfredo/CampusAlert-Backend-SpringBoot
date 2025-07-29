package com.kafka.userservice.services.impl;

import com.kafka.userservice.domain.enums.TypeToken;
import com.kafka.userservice.domain.models.Token;
import com.kafka.userservice.domain.models.User;
import com.kafka.userservice.repositories.TokenRepository;
import com.kafka.userservice.services.contract.TokenService;
import com.kafka.userservice.utils.token.factory.TokenProviderFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;
    private final TokenProviderFactory tokenProviderFactory;

    public TokenServiceImpl(TokenRepository tokenRepository, TokenProviderFactory tokenProviderFactory) {
        this.tokenRepository = tokenRepository;
        this.tokenProviderFactory = tokenProviderFactory;
    }

    @Override
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public void cleanTokens(TypeToken type, User user) {
        List<Token> tokens = tokenRepository.findByTypeTokenAndUserAndCanActive(type, user, true);

        for(Token t: tokens){
           if(!tokenProviderFactory.of(TypeToken.AUTH_TOKEN).validateToken(t.getToken())){
               t.setCanActive(false);
               tokenRepository.save(t);
           }
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Token registerToken(User user, TypeToken type) {
        try{
            cleanTokens(type, user);
            String stringToken = tokenProviderFactory.of(type).generateToken(user);
            Token token = new Token(stringToken, type, user);

            tokenRepository.save(token);

            return token;
        }catch (Exception e){
            throw new RuntimeException("Error al generar el token: "+ e.getMessage());
        }
    }

    @Override
    public String getEmailFromToken(String token, TypeToken type) {
        try{
            String email = tokenProviderFactory.of(type).getEmailFromToken(token);
            if(email == null)
                throw new Exception("El token no es valido");
            return email;
        }catch (Exception e){
            throw new RuntimeException("Error al obtener el email del token: " + e.getMessage());
        }
    }


}
