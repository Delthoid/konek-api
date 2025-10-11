package dev.delts.konek_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "servers")
@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
public @Data class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;
    private String description;

    @Column(nullable = false)
    private UUID ownerId;
    private String iconUrl;

    @CreatedDate
    private Instant createdAt;
    private Instant deletedAt;
}
