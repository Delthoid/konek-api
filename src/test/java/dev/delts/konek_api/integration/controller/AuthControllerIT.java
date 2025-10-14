package dev.delts.konek_api.integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.delts.konek_api.dto.auth.LoginRequest;
import dev.delts.konek_api.dto.auth.SignUpRequest;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void shouldRegisterUserSuccessfully_whenSignupRequestIsValid() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserName("testUser1");
        signUpRequest.setEmail("test1@mail.com");
        signUpRequest.setPassword("password1234");
        signUpRequest.setAvatarUrl("http://example.com/avatar.png");

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userName").value("testUser1"))
                .andExpect(jsonPath("$.email").value("test1@mail.com"));
    }

    @Test
    void shouldReturnError_whenUsernameOrPasswordIsAlreadyTaken() throws Exception {
        SignUpRequest firstUser = new SignUpRequest();
        firstUser.setUserName("user1");
        firstUser.setEmail("user1@mail.com");
        firstUser.setPassword("password1234");
        firstUser.setAvatarUrl("http://example.com/avatar.png");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstUser)));

        SignUpRequest secondUser = new SignUpRequest();
        secondUser.setUserName("user1");
        secondUser.setEmail("user1@mail.com");
        secondUser.setPassword("password1234");
        secondUser.setAvatarUrl("http://example.com/avatar.png");

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(secondUser)))
                .andExpect(status().isConflict());

        // Will return an error as well when username is existing already even using different email
        SignUpRequest thirdUser = new SignUpRequest();
        thirdUser.setUserName("user1");
        thirdUser.setEmail("user2@mail.com");
        thirdUser.setPassword("password1234");
        thirdUser.setAvatarUrl("http://example.com/avatar.png");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(thirdUser)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldReturnValidationError_whenSignUpRequestFieldsAreEmpty() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserName("");
        signUpRequest.setEmail("");
        signUpRequest.setPassword("123456");
        signUpRequest.setAvatarUrl("http://example.com/avatar.png");

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.userName").value("Username cannot be empty"))
                .andExpect(jsonPath("$.fieldErrors.email").value("Email cannot be empty"))
                .andExpect(jsonPath("$.fieldErrors.password").value("Password must be between 8 and 50 characters"));
    }

    @Test
    void shouldLoginUserSuccessfully_whenSignUpRequestIsSuccessful() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserName("user1");
        signUpRequest.setEmail("user1@mail.com");
        signUpRequest.setPassword("12345678");
        signUpRequest.setAvatarUrl("http://example.com/avatar.png");

        mockMvc.perform(post("/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserName("user1");
        loginRequest.setPassword("12345678");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userDtoResponse.userName").value("user1"))
                .andExpect(jsonPath("$.userDtoResponse.email").value("user1@mail.com"))
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void shouldReturnError_whenUserDoesNotExist() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserName("user1");
        signUpRequest.setEmail("user1@mail.com");
        signUpRequest.setPassword("12345678");
        signUpRequest.setAvatarUrl("http://example.com/avatar.png");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldReturnError_whenCredentialIsIncorrect() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUserName("user1");
        signUpRequest.setEmail("user1@mail.com");
        signUpRequest.setPassword("12345678");
        signUpRequest.setAvatarUrl("http://example.com/avatar.png");

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest1 = new LoginRequest();
        loginRequest1.setUserName("user1");
        loginRequest1.setPassword("12345678910");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest1)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Incorrect username or password"));
    }
}
