package com.kafka.notificationservice.utils.jwt.contract;

import com.kafka.notificationservice.domain.dtos.UserDto;

public interface IAuthProvider {
    boolean isValidToken(String token);
    UserDto getUserFromToken(String token);
}
