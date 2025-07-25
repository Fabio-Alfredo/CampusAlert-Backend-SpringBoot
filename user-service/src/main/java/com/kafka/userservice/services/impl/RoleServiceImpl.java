package com.kafka.userservice.services.impl;

import com.kafka.userservice.domain.models.Role;
import com.kafka.userservice.repositories.RoleRepository;
import com.kafka.userservice.services.contract.RoleService;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findById(String id) {
        try{
            var role = roleRepository.findById(id).orElse(null);
            if(role == null)
                throw new Exception("El role solicitado no existe");

            return role;
        }catch (Exception e){
            throw  new RuntimeException("Error al encontrar el email");
        }
    }
}
