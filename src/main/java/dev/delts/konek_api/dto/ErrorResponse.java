package dev.delts.konek_api.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

public @Data class ErrorResponse {
    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private Map<String, String> fieldErrors;
}
