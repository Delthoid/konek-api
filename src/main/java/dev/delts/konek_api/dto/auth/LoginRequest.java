package dev.delts.konek_api.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

public @Data class LoginRequest {
    @NotBlank(message = "Username cannot be empty")
    @Size(max = 24, message = "Max of 24 characters")
    private String userName;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, max = 50, message = "Password must be between 8 and 50 characters")
    private String password;
}
