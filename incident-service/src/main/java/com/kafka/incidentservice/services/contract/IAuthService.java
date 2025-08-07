package com.kafka.incidentservice.services.contract;


import com.kafka.incidentservice.domain.dtos.auth.UserDto;

public interface IAuthService {

    UserDto findAuthenticatedUser();
}
