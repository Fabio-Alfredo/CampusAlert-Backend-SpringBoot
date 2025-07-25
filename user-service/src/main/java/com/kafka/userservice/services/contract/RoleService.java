package com.kafka.userservice.services.contract;

import com.kafka.userservice.domain.models.Role;

public interface RoleService {

    Role findById(String id);
}
