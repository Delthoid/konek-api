package dev.delts.konek_api.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
public @Data class MemberResponseDto {
    private String role;
    private UserMemberResponseDto user;
    private Instant createdAt;
}
