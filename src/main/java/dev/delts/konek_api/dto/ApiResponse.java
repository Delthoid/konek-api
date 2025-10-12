package dev.delts.konek_api.dto;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {
    private final HttpStatus status;
    private final String message;
    private final String error;
    private final T data;

    public ApiResponse(HttpStatus status, String message, String error, T data) {
        this.status = status;
        this.message = message;
        this.error = error;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, null, data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.OK, message, null, data);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(HttpStatus.OK, "Operation successful", null, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(HttpStatus.OK, message, null, null);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String errorMessage) {
        return new ApiResponse<>(status, null, errorMessage, null);
    }

    public static <T> ApiResponse<T> error(HttpStatus status, String errorMessage, T data) {
        return new ApiResponse<>(status, null, errorMessage, data);
    }

    public static <T> ApiResponse<T> error(String errorMessage) {
        return error(HttpStatus.INTERNAL_SERVER_ERROR, errorMessage);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    public T getData() {
        return data;
    }
}
