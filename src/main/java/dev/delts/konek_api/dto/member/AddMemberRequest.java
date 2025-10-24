package dev.delts.konek_api.dto.member;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
public @Data class AddMemberRequest {
    @NotBlank
    private UUID userId;

    @Builder.Default
    private String role = "MEMBER";
}
