package com.kafka.userservice.controllers;

import com.kafka.userservice.domain.dtos.GeneralResponse;
import com.kafka.userservice.domain.dtos.LocalAuthDto;
import com.kafka.userservice.domain.dtos.RegisterUserDto;
import com.kafka.userservice.domain.dtos.TokenDto;
import com.kafka.userservice.domain.models.Token;
import com.kafka.userservice.services.contract.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
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

}
