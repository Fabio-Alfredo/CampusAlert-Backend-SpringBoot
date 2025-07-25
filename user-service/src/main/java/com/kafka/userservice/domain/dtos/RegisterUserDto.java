package com.kafka.userservice.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterUserDto {

    @NotEmpty(message = "El username es necesario")
    private String userName;
    @NotEmpty
    @Email(message = "Email enviado es invalido")
    private String email;
    @NotEmpty(message = "La contraseña es requerida")
    private String password;


}
