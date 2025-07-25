package com.kafka.userservice.services.contract;


import com.kafka.userservice.domain.dtos.RegisterUserDto;
import com.kafka.userservice.domain.models.User;

public interface UserService {

    User findByEmail(String email);
    User findByIdentifier(String identifier);
    void registerUser(RegisterUserDto userDto);
}
