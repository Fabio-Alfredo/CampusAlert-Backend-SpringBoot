package com.kafka.incidentservice.services.impl;

import com.kafka.incidentservice.domain.dtos.auth.UserDto;
import com.kafka.incidentservice.services.contract.IAuthService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {
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
}
