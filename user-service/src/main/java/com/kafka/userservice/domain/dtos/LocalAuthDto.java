package com.kafka.userservice.domain.dtos;

import jakarta.validation.constraints.NotEmpty;

public class LocalAuthDto {
    @NotEmpty(message = "El indentificador es requerido")
    private String identifier;
    @NotEmpty(message = "La contraseña es requerida")
    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
