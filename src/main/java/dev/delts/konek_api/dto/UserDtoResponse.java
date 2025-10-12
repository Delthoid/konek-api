package dev.delts.konek_api.dto;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

public @Data class UserDtoResponse {
    private UUID userId;
    private String userName;
    private String email;
    private String avatarUrl;
    private Instant createdAt;
}
