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
    private String photo;

    public RegisterUserDto(String email, String userName, String password, String photo) {
        this.email = email;
        this.userName = userName;
        this.password = password;
        this.photo = photo;
    }

    public RegisterUserDto() {
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

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
