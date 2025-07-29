package com.kafka.userservice.services.contract;

import com.kafka.userservice.domain.dtos.auth.RegisterUserDto;

public interface ExternalAuthProviderService {
    RegisterUserDto fetchUserData(String token);

}
