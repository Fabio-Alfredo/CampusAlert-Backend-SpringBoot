package com.kafka.auditservice.domain.dtos.auth;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class UserDto {
    private UUID id;
    private String email;
    private List<String>roles;

    public Collection<GrantedAuthority>getAuthorities(){
        List<GrantedAuthority>authorities = new ArrayList<>();
       authorities =  roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
       return authorities;
    }

}
