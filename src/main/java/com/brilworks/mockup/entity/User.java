package com.brilworks.mockup.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Table(name = "users")
@Entity
@NoArgsConstructor
public class User extends BaseEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "email")
    private String email;

    public User(int userId) {
        this.id = userId;
    }

    public User(Integer id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
}
