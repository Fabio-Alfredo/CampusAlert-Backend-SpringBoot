package com.kafka.userservice.repositories;

import com.kafka.userservice.domain.models.Role;
import com.kafka.userservice.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    User findByEmailOrUserName(String email, String username);
    User findByEmail(String email);
    User findByIdAndRoles(UUID id, List<Role> roles);
}

