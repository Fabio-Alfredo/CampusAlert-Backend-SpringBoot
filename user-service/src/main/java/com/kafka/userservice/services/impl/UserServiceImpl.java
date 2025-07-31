package com.kafka.userservice.services.impl;

import com.kafka.userservice.domain.dtos.auth.*;
import com.kafka.userservice.domain.dtos.commons.KafkaEvents;
import com.kafka.userservice.domain.enums.AuthProvider;
import com.kafka.userservice.domain.enums.KafkaEventTypes;
import com.kafka.userservice.domain.enums.TypeToken;
import com.kafka.userservice.domain.models.Token;
import com.kafka.userservice.domain.models.User;
import com.kafka.userservice.repositories.UserRepository;
import com.kafka.userservice.services.contract.RoleService;
import com.kafka.userservice.services.contract.TokenService;
import com.kafka.userservice.services.contract.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    @Value("${auth.roles.default-role}")
    private String defaultRole;
    @Value("${kafka.topic.user-and-audit.name}")
    private String userAndAuditTopic;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final TokenService tokenService;
    private final GoogleAuthServiceImpl googleAuthService;
    private final EmailServiceImpl emailService;
    private final KafkaTemplate<String, KafkaEvents<UserRegisterAuditDto>> kafkaTemplate;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService, TokenService tokenService, GoogleAuthServiceImpl googleAuthService, EmailServiceImpl emailService, KafkaTemplate<String, KafkaEvents<UserRegisterAuditDto>> kafkaTemplate) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.tokenService = tokenService;
        this.googleAuthService = googleAuthService;
        this.emailService = emailService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
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
           newUser.setPhoto(userDto.getPhoto());
           newUser.setAuthProvider(AuthProvider.LOCAL);

           userRepository.save(newUser);
           sendUserRegisterAudit(newUser);
        }catch (Exception e){
            throw new RuntimeException("Erro al registrar el usuario");
        }
    }

    private void sendUserRegisterAudit(User user){
        UserRegisterAuditDto data = new UserRegisterAuditDto();
        data.setEventType(KafkaEventTypes.USER_REGISTERED);
        data.setPayload(user.getEmail() + " - " + user.getId());

        kafkaTemplate.send(userAndAuditTopic, new KafkaEvents<>(KafkaEventTypes.USER_REGISTERED, data));
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

    @Override
    public Token googleAuth(TokenDto tokenDto) {
        try{
            RegisterUserDto userDto = googleAuthService.fetchUserData(tokenDto.getToken());
            User user = userRepository.findByEmail(userDto.getEmail());

            if (user == null) {
                var role = roleService.findById(defaultRole);

                user = new User();
                user.setUserName(userDto.getUserName());
                user.setEmail(userDto.getEmail());
                user.setPhoto(userDto.getPhoto());
                user.setAuthProvider(AuthProvider.GOOGLE_PROVIDER);
                user.setRoles(List.of(role));
                user = userRepository.save(user);
            }


            if (!AuthProvider.GOOGLE_PROVIDER.equals(user.getAuthProvider())) {
                throw new RuntimeException("Usuario registrado con un método diferente");
            }

            return tokenService.registerToken(user, TypeToken.AUTH_TOKEN);
        }catch (Exception e){
            throw new RuntimeException("Error al iniciar sesion: "+e.getMessage());
        }
    }

    @Override
    public void forgotPassword(String email) {
        try{
            var user = userRepository.findByEmail(email);
            if(user == null || user.getAuthProvider() != AuthProvider.LOCAL)
                throw new Exception("El usuario no existe o no es un usuario local");

            String token = tokenService.registerToken(user, TypeToken.RESET_PASSWORD_TOKEN).getToken();
            emailService.sendRecoveryEmail(
                    user.getEmail(),
                    token
            );
        }catch (Exception e){
            throw new  RuntimeException("Error al enviar el correo de recuperacion: " + e.getMessage());
        }
    }

    @Override
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        try{
            String email = tokenService.getEmailFromToken(resetPasswordDto.getToken(), TypeToken.RESET_PASSWORD_TOKEN);
            User user = userRepository.findByEmail(email);
            if(user == null || !tokenService.isValidToken(user, TypeToken.RESET_PASSWORD_TOKEN, resetPasswordDto.getToken()))
                throw new Exception("El token no es valido ");

            user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
            userRepository.save(user);
        }catch (Exception e){
            throw new RuntimeException("Error al restablecer la contraseña: " + e.getMessage());
        }
    }

    @Override
    public User findById(UUID id) {
        try{
            var user = userRepository.findById(id).orElse(null);
            if(user == null)
                throw new Exception("El usuario no existe");

            return user;
        }catch (Exception e){
            throw new RuntimeException("Error al buscar el usuario por ID: " + e.getMessage());
        }
    }

    @Override
    public List<User> findAllUsers() {
        try{
            List<User> users = userRepository.findAll();
            if(users.isEmpty())
                throw new Exception("No hay usuarios registrados");

            return users;
        }catch (Exception e ){
            throw  new RuntimeException("Error al buscar todos los usuarios: " + e.getMessage());
        }
    }

    @Override
    public User getUserAuthenticated() {
        try{
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(user == null)
                throw new Exception("No hay un usuario autenticado");
            return user;
        }catch (Exception e){
            throw new RuntimeException("Error al obtener el usuario autenticado: " + e.getMessage());
        }
    }

}
