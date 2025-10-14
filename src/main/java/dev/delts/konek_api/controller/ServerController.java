package dev.delts.konek_api.controller;

import dev.delts.konek_api.dto.ApiResponse;
import dev.delts.konek_api.dto.request.server.ServerCreateRequest;
import dev.delts.konek_api.dto.request.server.ServerUpdateRequest;
import dev.delts.konek_api.entity.Server;
import dev.delts.konek_api.service.AuthService;
import dev.delts.konek_api.service.ServerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/server")
public class ServerController {

    private final AuthService authService;
    private final ServerService serverService;

    public ServerController(AuthService authService, ServerService serverService) {
        this.authService = authService;
        this.serverService = serverService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Server>> create(@Valid @RequestBody ServerCreateRequest request, HttpServletRequest servletRequest) {
        UUID userId = authService.getUserIdFromRequest(servletRequest);
        Server savedServer = serverService.create(request, userId);

        return ResponseEntity.ok(ApiResponse.success("Server created successfully", savedServer));
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<Server>> update(@Valid @RequestBody ServerUpdateRequest request, HttpServletRequest servletRequest) {
        UUID userId = authService.getUserIdFromRequest(servletRequest);
        Server updatedServer = serverService.update(request, userId);

        return ResponseEntity.ok(ApiResponse.success("Server updated successfully", updatedServer));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<?>> delete(@RequestParam UUID serverId, HttpServletRequest servletRequest) {
        UUID userId = authService.getUserIdFromRequest(servletRequest);
        serverService.delete(serverId, userId);

        return ResponseEntity.ok(ApiResponse.success("Server deleted successfully"));
    }

    @GetMapping("/servers")
    public ResponseEntity<ApiResponse<?>> getServers(@RequestParam(required = false) String query, HttpServletRequest servletRequest) {
        UUID userId = authService.getUserIdFromRequest(servletRequest);

        List<Server> serverList = serverService.findByUserId(userId);

        return ResponseEntity.ok(ApiResponse.success(serverList));
    }

}
