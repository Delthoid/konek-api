package dev.delts.konek_api.dto.request.server;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

public @Data class ServerCreateRequest {

    @NotBlank
    @Size(min = 4, max = 100, message = "Server name must be between 4 and 100 characters")
    private String name;

    @Size(min = 10, max = 500, message = "Server description must be between 10 and 500 characters")
    private String description;

    @JsonIgnore
    private UUID ownerId;

    private String iconUrl;
}
