package com.kafka.userservice.domain.models;

import com.kafka.userservice.domain.enums.TypeToken;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column( length = 1024, nullable = false)
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


    public Token(String token,TypeToken typeToken, User user) {
        this.token = token;
        this.canActive = true;
        this.typeToken = typeToken;
        this.user = user;
    }

    public Token() {
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public TypeToken getTypeToken() {
        return typeToken;
    }

    public void setTypeToken(TypeToken typeToken) {
        this.typeToken = typeToken;
    }

    public boolean isCanActive() {
        return canActive;
    }

    public void setCanActive(boolean canActive) {
        this.canActive = canActive;
    }

    public Date getTimesTamp() {
        return timesTamp;
    }

    public void setTimesTamp(Date timesTamp) {
        this.timesTamp = timesTamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
