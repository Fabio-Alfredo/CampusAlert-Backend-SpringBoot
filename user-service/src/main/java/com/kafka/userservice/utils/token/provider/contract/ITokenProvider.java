package com.kafka.userservice.utils.token.provider.contract;

import com.kafka.userservice.domain.enums.TypeToken;
import com.kafka.userservice.domain.models.User;

public interface ITokenProvider {
    TypeToken supportedTypeToken();
    String generateToken(User user);
    String getEmailFromToken(String token);
    boolean validateToken(String token);

}
