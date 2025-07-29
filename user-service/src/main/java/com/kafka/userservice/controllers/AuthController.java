package com.kafka.userservice.controllers;

import com.kafka.userservice.domain.dtos.auth.*;
import com.kafka.userservice.domain.dtos.commons.GeneralResponse;
import com.kafka.userservice.domain.models.Token;
import com.kafka.userservice.services.contract.UserService;
import com.kafka.userservice.services.impl.GoogleAuthServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final GoogleAuthServiceImpl googleAuthService;

    public AuthController(UserService userService, GoogleAuthServiceImpl googleAuthService) {
        this.userService = userService;
        this.googleAuthService = googleAuthService;
    }

    @PostMapping("/register")
    public ResponseEntity<GeneralResponse> registerUser(@RequestBody @Valid RegisterUserDto userDto){
        try{
           userService.registerUser(userDto);
           return GeneralResponse.getResponse(HttpStatus.CREATED, "Usuario registrado con exito");
        }catch (Exception e){
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error al registrar usuario: "+e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse> loginUser(@RequestBody @Valid LocalAuthDto loginLocal){
        try{
            Token token = userService.localAuth(loginLocal);
            return GeneralResponse.getResponse(HttpStatus.CREATED, "Has iniciado sesion con exito",new TokenDto(token.getToken()));
        }catch (Exception e){
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error al iniciar sesion: "+e.getMessage());
        }
    }

    @PostMapping("google-auth")
    public ResponseEntity<GeneralResponse> loginUser(@RequestBody TokenDto tokenDto){
        try{
            Token token = userService.googleAuth(tokenDto);
            return GeneralResponse.getResponse(HttpStatus.CREATED, "Has iniciado sesion con exito",new TokenDto(token.getToken()));
        }catch (Exception e){
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error al iniciar sesion: "+e.getMessage());
        }
    }

    @PutMapping("/forgot-password")
    public ResponseEntity<GeneralResponse>forgotPassword(@RequestBody EmailDto emailDto){
        try{
            userService.forgotPassword(emailDto.getEmail());
            return GeneralResponse.getResponse(HttpStatus.OK, "Correo de recuperacion enviado con exito");
        }catch (Exception e){
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error al enviar el correo de recuperacion: "+e.getMessage());
        }
    }

    @PutMapping("/reset-password")
    public ResponseEntity<GeneralResponse>resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto){
        try{
            userService.resetPassword(resetPasswordDto);
            return GeneralResponse.getResponse(HttpStatus.OK, "Contraseña restablecida con exito");
        }catch (Exception e){
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error al restablecer la contraseña: "+e.getMessage());
        }
    }

}
