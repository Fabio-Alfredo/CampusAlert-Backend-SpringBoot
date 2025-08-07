package com.kafka.incidentservice.services.contract;


import com.kafka.incidentservice.domain.dtos.auth.UserDto;

import java.util.UUID;

public interface IAuthService {

    UserDto findAuthenticatedUser();
    boolean isValidUser(UUID userId);
}
