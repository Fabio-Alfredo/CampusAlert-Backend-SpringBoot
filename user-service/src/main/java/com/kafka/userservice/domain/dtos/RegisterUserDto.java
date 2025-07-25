package com.kafka.userservice.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;


public class RegisterUserDto {

    @NotEmpty(message = "El username es necesario")
    private String userName;
    @NotEmpty
    @Email(message = "Email enviado es invalido")
    private String email;
    @NotEmpty(message = "La contraseña es requerida")
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
