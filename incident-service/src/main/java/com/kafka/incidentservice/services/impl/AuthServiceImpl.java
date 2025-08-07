package com.kafka.incidentservice.services.impl;

import com.kafka.incidentservice.domain.dtos.auth.UserDto;
import com.kafka.incidentservice.domain.dtos.common.GeneralResponse;
import com.kafka.incidentservice.services.contract.IAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.ParameterizedType;
import java.util.UUID;

@Service
public class AuthServiceImpl implements IAuthService {

    @Value("${user.service.url}")
    private String userServiceUrl;

    private final RestTemplate restTemplate;

    public AuthServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public UserDto findAuthenticatedUser() {
        try{
            UserDto user = (UserDto) SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getPrincipal();

            return user;
        }catch (Exception e){
            throw new RuntimeException("Error finding authenticated user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean isValidUser(UUID userId) {
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + getToken());

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<GeneralResponse> res = restTemplate.exchange(
                    userServiceUrl+"/exists/" + userId,
                    HttpMethod.GET,
                    entity,
                    GeneralResponse.class
            );

            if (res.getStatusCode().is2xxSuccessful()) {
                GeneralResponse response = res.getBody();
                return response.getData() != null && (boolean) response.getData();
            }
            return false;

        }catch (Exception e){
            throw new RuntimeException("Error validating user: " + e.getMessage(), e);
        }
    }

    private String getToken() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            if (auth != null && auth.getCredentials() != null) {
                return auth.getCredentials().toString();
            }

            throw new RuntimeException("No token found in credentials");
        } catch (Exception e) {
            throw new RuntimeException("Error getting token: " + e.getMessage(), e);
        }
    }

}
