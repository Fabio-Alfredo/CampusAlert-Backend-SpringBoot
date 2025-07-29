package com.kafka.userservice.services.contract;


import com.kafka.userservice.domain.dtos.LocalAuthDto;
import com.kafka.userservice.domain.dtos.RegisterUserDto;
import com.kafka.userservice.domain.dtos.ResetPasswordDto;
import com.kafka.userservice.domain.dtos.TokenDto;
import com.kafka.userservice.domain.models.Token;
import com.kafka.userservice.domain.models.User;

public interface UserService {

    User findByEmail(String email);
    User findByIdentifier(String identifier);
    void registerUser(RegisterUserDto userDto);
    Token localAuth(LocalAuthDto loginDto);
    Token googleAuth(TokenDto tokenDto);
    void forgotPassword(String email);
    void resetPassword(ResetPasswordDto resetPasswordDto);

}
