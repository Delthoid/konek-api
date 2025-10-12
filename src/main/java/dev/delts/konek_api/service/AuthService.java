package dev.delts.konek_api.service;

import dev.delts.konek_api.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {
    private final JwtUtil jwtUtil;

    public AuthService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public UUID getUserIdFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid JWT token");
        }

        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);

        return UUID.fromString(userId);
    }
}
