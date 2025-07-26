package com.kafka.userservice.controllers;

import com.kafka.userservice.domain.dtos.LocalAuthDto;
import com.kafka.userservice.domain.dtos.RegisterUserDto;
import com.kafka.userservice.domain.models.Token;
import com.kafka.userservice.services.contract.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<String> registerUser(@RequestBody @Valid RegisterUserDto userDto){
        try{
           userService.registerUser(userDto);
           return ResponseEntity.ok("Usuario registrado con exito");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error al registrar el usuario: "+e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LocalAuthDto loginLocal){
        try{
            Token token = userService.localAuth(loginLocal);
            return ResponseEntity.ok("Has iniciado sesion con exito: "+token.getToken());
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error al iniciar sesion: "+e.getMessage());
        }
    }

}
