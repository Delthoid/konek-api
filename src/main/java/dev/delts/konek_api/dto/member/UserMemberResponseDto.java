package dev.delts.konek_api.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
public @Data class UserMemberResponseDto {
    private UUID id;
    private String userName;
    private String avatarUrl;
}
