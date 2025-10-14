package dev.delts.konek_api;

import dev.delts.konek_api.controller.AuthController;
import dev.delts.konek_api.controller.ServerController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class SmokeTest {
    @Autowired
    private AuthController authController;

    @Autowired
    private ServerController serverController;

    @Test
    void contextLoads() throws Exception {
        assertThat(authController).isNotNull();
        assertThat(serverController).isNotNull();
    }
}
