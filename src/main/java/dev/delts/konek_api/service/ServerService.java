package dev.delts.konek_api.service;

import dev.delts.konek_api.dto.request.server.ServerCreateRequest;
import dev.delts.konek_api.dto.request.server.ServerUpdateRequest;
import dev.delts.konek_api.entity.Server;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServerService {
    Optional<Server> getById(UUID id);
    Server create(ServerCreateRequest request, UUID userId);
    Server update(ServerUpdateRequest request, UUID userId);
    void delete(UUID id, UUID userId);

    Optional<Server> findByNameAndOwnerId(String name, UUID ownerId);
    List<Server> findByUserId(UUID userId);
}
