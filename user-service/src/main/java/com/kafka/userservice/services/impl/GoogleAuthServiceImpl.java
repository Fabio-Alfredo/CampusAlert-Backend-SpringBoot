package com.kafka.userservice.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.kafka.userservice.domain.dtos.RegisterUserDto;
import com.kafka.userservice.services.contract.ExternalAuthProviderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;


@Service
public class GoogleAuthServiceImpl implements ExternalAuthProviderService {

    @Value("${google.auth.google-id}")
    private String CLIENT_ID;

    @Override
    public RegisterUserDto fetchUserData(String token){
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            RegisterUserDto userDto = new RegisterUserDto();
            if (idToken != null) {
                 Payload payload = idToken.getPayload();
                 userDto.setEmail(payload.getEmail());
                 userDto.setUserName((String) payload.get("name"));
                userDto.setPhoto((String) payload.get("picture"));

            } else {
                throw new Exception("token invalido");
            }
            return userDto;
        }catch (Exception e) {
            throw  new RuntimeException("Error: "+e.getMessage());
        }
    }
}
