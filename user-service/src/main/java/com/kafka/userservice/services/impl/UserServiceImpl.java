package com.kafka.userservice.services.impl;

import com.kafka.userservice.domain.dtos.LocalAuthDto;
import com.kafka.userservice.domain.dtos.RegisterUserDto;
import com.kafka.userservice.domain.enums.AuthProvider;
import com.kafka.userservice.domain.enums.TypeToken;
import com.kafka.userservice.domain.models.Token;
import com.kafka.userservice.domain.models.User;
import com.kafka.userservice.repositories.TokenRepository;
import com.kafka.userservice.repositories.UserRepository;
import com.kafka.userservice.services.contract.RoleService;
import com.kafka.userservice.services.contract.TokenService;
import com.kafka.userservice.services.contract.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {


    @Value("${auth.roles.default-role}")
    private String defaultRole;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final TokenService tokenService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.tokenService = tokenService;
    }

    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        try{
            var user = userRepository.findByEmail(email);
            if(user == null)
                throw new Exception("El usuario no existe");

            return user;
        }catch (Exception e){
            throw  new RuntimeException("Error al buscar el usuario");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public User findByIdentifier(String identifier) {
        try {
            var user  = userRepository.findByEmailOrUserName(identifier, identifier);
            if(user == null)
                throw new Exception("El usuario no existe");

            return user;
        }catch (Exception e){
            throw new RuntimeException("Error al buscar el usuario");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerUser(RegisterUserDto userDto) {
        try{
           var user = userRepository.findByEmailOrUserName(userDto.getEmail(), userDto.getEmail());
           if(user != null)
               throw new Exception("El usuario ya ha sido registrado");

           var role = roleService.findById(defaultRole);

           User newUser = new User();
           newUser.setUserName(userDto.getUserName());
           newUser.setEmail(userDto.getEmail());
           newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
           newUser.setRoles(List.of(role));
           newUser.setAuthProvider(AuthProvider.LOCAL);

           userRepository.save(newUser);
        }catch (Exception e){
            throw new RuntimeException("Erro al registrar el usuario");
        }
    }

    @Override
    public Token localAuth(LocalAuthDto loginDto) {
        try{
            User user = userRepository.findByEmailOrUserName(loginDto.getIdentifier(), loginDto.getIdentifier());
            if(user == null || !passwordEncoder.matches(loginDto.getPassword(), user.getPassword()))
                throw new Exception("Credencilaes invalidas");

            return  tokenService.registerToken(user, TypeToken.AUTH_TOKEN);
        }catch (Exception e){
            throw new RuntimeException("Error al iniciar sesion "+e.getMessage());
        }
    }
}
