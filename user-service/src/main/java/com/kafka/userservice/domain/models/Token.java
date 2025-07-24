package com.kafka.userservice.domain.models;

import com.kafka.userservice.domain.enums.TypeToken;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private TypeToken typeToken;
    private boolean canActive;
    private Date timesTamp;

    @PrePersist
    public void prePersist(){
        this.timesTamp = Date.from(Instant.now());
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;
}
