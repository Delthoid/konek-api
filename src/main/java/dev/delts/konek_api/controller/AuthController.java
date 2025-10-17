package dev.delts.konek_api.controller;

import dev.delts.konek_api.dto.ApiResponse;
import dev.delts.konek_api.dto.request.auth.LoginRequest;
import dev.delts.konek_api.dto.auth.LoginResponse;
import dev.delts.konek_api.dto.request.auth.SignUpRequest;
import dev.delts.konek_api.dto.UserDtoResponse;
import dev.delts.konek_api.service.UserService;
import dev.delts.konek_api.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<?>> register(@Valid @RequestBody SignUpRequest request) {
        UserDtoResponse userDtoResponse = userService.signUp(request);
        return new ResponseEntity<>(ApiResponse.success("Registered successfully", userDtoResponse), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@RequestBody LoginRequest request) {
        LoginResponse loginResponse = userService.login(request);
        return new ResponseEntity<>(ApiResponse.success("Logged-in", loginResponse), HttpStatus.OK);
    }
}
