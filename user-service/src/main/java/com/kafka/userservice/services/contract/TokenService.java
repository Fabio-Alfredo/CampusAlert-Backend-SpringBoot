package com.kafka.userservice.services.contract;

import com.kafka.userservice.domain.enums.TypeToken;
import com.kafka.userservice.domain.models.Token;
import com.kafka.userservice.domain.models.User;

public interface TokenService {
    boolean isValidToken(User user, TypeToken type, String token);
    void cleanTokens(TypeToken type, User user);
    Token registerToken(User user, TypeToken type);
}
