package hu.unideb.cartshare.model.entity;

import hu.unideb.cartshare.model.entity.common.BaseEntity;
import hu.unideb.cartshare.model.enums.AuthProvider;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

/**
 * Spring Data JPA user entity with a table name: users.
 */
@Getter
@Setter
@Entity(name = "users")
public class User extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'LOCAL'")
    private AuthProvider provider;

    private String providerId;
}
