package com.kafka.notificationservice.domain.dtos;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserDto {
    private UUID id;
    private String email;
    private List<String>roles;
}
