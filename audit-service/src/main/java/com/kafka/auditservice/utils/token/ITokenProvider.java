package com.kafka.auditservice.utils.token;

import com.kafka.auditservice.domain.dtos.auth.UserDto;

public interface ITokenProvider {
    boolean isValidToken(String token);
    UserDto getUserFromToken(String token);
}
