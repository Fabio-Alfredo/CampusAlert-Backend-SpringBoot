package com.kafka.userservice.services.impl;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.kafka.userservice.domain.dtos.RegisterUserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Collections;


@Service
public class GoogleAuthService {

    @Value("${google.auth.google-id}")
    private String CLIENT_ID;

    public RegisterUserDto fetchGoogleUserData(String token) throws GeneralSecurityException, IOException {
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
