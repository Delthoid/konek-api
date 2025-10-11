package dev.delts.konek_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "users")
@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
public @Data class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String email;
    //TODO: Hashed password
    private String password;
    private String avatarUrl;

    @CreatedDate
    private Instant createdAt;
    private Instant deletedAt;
}
