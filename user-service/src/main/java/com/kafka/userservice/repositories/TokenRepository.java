package com.kafka.userservice.repositories;

import com.kafka.userservice.domain.enums.TypeToken;
import com.kafka.userservice.domain.models.Token;
import com.kafka.userservice.domain.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TokenRepository extends JpaRepository<Token, UUID> {
    List<Token>findByTypeTokenAndUserAndCanActive(TypeToken typeToken, User user, boolean canActive);
}
