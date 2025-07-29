package com.kafka.userservice.services.contract;

import com.kafka.userservice.domain.dtos.RegisterUserDto;

public interface ExternalAuthProviderService {
    RegisterUserDto fetchUserData(String token);

}
