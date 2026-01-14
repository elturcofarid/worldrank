package com.worldrank.app.user.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "usuario")
@Getter @Setter
@NoArgsConstructor
public class Usuario {

    public Usuario(UUID id, String email, String username, String password, String country) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.password = password;
        this.country = country;
    }

    @Id
    @Column(name = "id_usuario")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String password;

    @Column(name = "pais_origen")
    private String country;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public UUID getId() {
        return id;
    }

}