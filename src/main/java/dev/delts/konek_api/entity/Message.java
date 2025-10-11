package dev.delts.konek_api.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "messages")
@EnableJpaAuditing
@EntityListeners(AuditingEntityListener.class)
public @Data class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID authorId;

    @Column(nullable = false)
    private UUID channelId;

    private String message;

    @CreatedDate
    private Instant createdAt;
}
