package com.kafka.incidentservice.uitls.jwt;

import com.kafka.incidentservice.domain.dtos.auth.UserDto;

public interface IAuthProvider {
    boolean isValidToken(String token);
    UserDto getUserFromToken(String token);

}
