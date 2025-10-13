package dev.delts.konek_api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.delts.konek_api.dto.ErrorResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // This is invoked when a user tries to access a secured resource without supplying any credentials
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimeStamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setError("Unauthorized");
        errorResponse.setMessage(authException.getMessage());
        errorResponse.setPath(request.getRequestURI());

        if (authException.getCause() instanceof ExpiredJwtException) {
            errorResponse.setError("JWT Expired");
            errorResponse.setMessage("Your token is expired, please login again");
        } else if (authException.getCause() instanceof MalformedJwtException) {
            errorResponse.setError("Invalid JWT");
            errorResponse.setMessage("Your token is invalid");
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();

        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}
