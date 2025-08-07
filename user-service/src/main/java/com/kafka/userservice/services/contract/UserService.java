package com.kafka.userservice.services.contract;


import com.kafka.userservice.domain.dtos.auth.LocalAuthDto;
import com.kafka.userservice.domain.dtos.auth.RegisterUserDto;
import com.kafka.userservice.domain.dtos.auth.ResetPasswordDto;
import com.kafka.userservice.domain.dtos.auth.TokenDto;
import com.kafka.userservice.domain.models.Token;
import com.kafka.userservice.domain.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User findByEmail(String email);
    User findByIdentifier(String identifier);
    void registerUser(RegisterUserDto userDto);
    Token localAuth(LocalAuthDto loginDto);
    Token googleAuth(TokenDto tokenDto);
    void forgotPassword(String email);
    void resetPassword(ResetPasswordDto resetPasswordDto);
    void updatePhoto(MultipartFile photoFile, String email);
    Boolean existUserById(UUID id);

    User findById(UUID id);
    List<User> findAllUsers();
    User getUserAuthenticated();

}
