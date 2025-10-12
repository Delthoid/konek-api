package dev.delts.konek_api.dto.auth;

import dev.delts.konek_api.dto.UserDtoResponse;
import lombok.Data;

public @Data class LoginResponse {
    private UserDtoResponse userDtoResponse;
    private String token;
}
