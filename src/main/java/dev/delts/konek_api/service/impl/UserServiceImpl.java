package dev.delts.konek_api.service.impl;

import dev.delts.konek_api.dto.UserDtoResponse;
import dev.delts.konek_api.dto.request.auth.LoginRequest;
import dev.delts.konek_api.dto.auth.LoginResponse;
import dev.delts.konek_api.dto.request.auth.SignUpRequest;
import dev.delts.konek_api.entity.User;
import dev.delts.konek_api.exception.RecordAlreadyExistsException;
import dev.delts.konek_api.repository.UserRepository;
import dev.delts.konek_api.service.UserService;
import dev.delts.konek_api.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public UserServiceImpl(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil
    ) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDtoResponse signUp(SignUpRequest signUpRequest) {

        userRepository.findByEmailOrUserName(signUpRequest.getEmail(), signUpRequest.getUserName())
                .ifPresent(user -> {
                    if (user.getUserName().trim().equals(signUpRequest.getUserName().trim())) {
                        throw new RecordAlreadyExistsException("Username already exists.");
                    } else {
                        throw new RecordAlreadyExistsException("Email already exists.");
                    }
                });

        User newUser = new User();
        newUser.setUserName(signUpRequest.getUserName());
        newUser.setEmail(signUpRequest.getEmail());
        newUser.setAvatarUrl(signUpRequest.getAvatarUrl());
        newUser.setCreatedAt(Instant.now());
        newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        User savedUser = userRepository.save(newUser);
        UserDtoResponse response = new UserDtoResponse();

        response.setUserId(savedUser.getId());
        response.setUserName(savedUser.getUserName());
        response.setEmail(savedUser.getEmail());
        response.setAvatarUrl(savedUser.getAvatarUrl());
        response.setCreatedAt(savedUser.getCreatedAt());

        return response;
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        LoginResponse loginResponse = new LoginResponse();
        UserDtoResponse userDtoResponse = new UserDtoResponse();

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getUserName());
        AtomicReference<String> token = new AtomicReference<>();

        userRepository.findByUserName(userDetails.getUsername())
                .ifPresent(user -> {
                    token.set(jwtUtil.generateToken(userDetails, user.getId()));

                    userDtoResponse.setUserId(user.getId());
                    userDtoResponse.setUserName(user.getUserName());
                    userDtoResponse.setEmail(user.getEmail());
                    userDtoResponse.setAvatarUrl(user.getAvatarUrl());
                    userDtoResponse.setCreatedAt(user.getCreatedAt());
                });

        loginResponse.setToken(token.get());
        loginResponse.setUserDtoResponse(userDtoResponse);

        return loginResponse;
    }
}
