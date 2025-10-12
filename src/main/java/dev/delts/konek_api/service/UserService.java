package dev.delts.konek_api.service;

import dev.delts.konek_api.dto.UserDtoResponse;
import dev.delts.konek_api.dto.request.auth.LoginRequest;
import dev.delts.konek_api.dto.auth.LoginResponse;
import dev.delts.konek_api.dto.request.auth.SignUpRequest;

public interface UserService {
    UserDtoResponse signUp(SignUpRequest request);
    LoginResponse login(LoginRequest request);
}
