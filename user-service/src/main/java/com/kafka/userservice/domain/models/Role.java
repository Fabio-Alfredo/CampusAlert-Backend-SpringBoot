package com.kafka.userservice.domain.models;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name="roles")
public class Role {

    @Id
    private String id;
    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User>users;

    public Role(String id, String name, List<User> users) {
        this.id = id;
        this.name = name;
        this.users = users;
    }


    public Role() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
