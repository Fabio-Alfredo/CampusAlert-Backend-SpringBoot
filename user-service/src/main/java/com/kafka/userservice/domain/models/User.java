package com.kafka.userservice.domain.models;

import com.kafka.userservice.domain.enums.AuthProvider;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false, unique = true)
    private String email;

    private String photo;
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private AuthProvider authProvider;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Token> tokens;

    @ManyToMany(fetch =  FetchType.LAZY)
    @JoinTable(
            name="users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role>roles;
}
