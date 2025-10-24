package dev.delts.konek_api.service.impl;

import dev.delts.konek_api.dto.request.server.ServerCreateRequest;
import dev.delts.konek_api.dto.request.server.ServerUpdateRequest;
import dev.delts.konek_api.entity.Server;
import dev.delts.konek_api.exception.RecordAlreadyExistsException;
import dev.delts.konek_api.exception.RecordNotFoundException;
import dev.delts.konek_api.repository.ServerRepository;
import dev.delts.konek_api.service.ServerService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServerServiceImpl implements ServerService {

    private final ServerRepository serverRepository;

    public ServerServiceImpl(ServerRepository serverRepository) {
        this.serverRepository = serverRepository;
    }

    @Override
    public Optional<Server> getById(UUID id) {
        return serverRepository.findById(id);
    }

    @Override
    public Server create(ServerCreateRequest request, UUID userId) {
        Server newServer = new Server();
        newServer.setName(request.getName());
        newServer.setDescription(request.getDescription());
        newServer.setOwnerId(userId);
        newServer.setIconUrl(request.getIconUrl());

        serverRepository.findByNameAndOwnerId(request.getName(), userId)
                .ifPresent(server -> {
                    throw new RecordAlreadyExistsException("Server name already exists.");
                });

        return serverRepository.save(newServer);
    }

    @Override
    public Server update(ServerUpdateRequest request, UUID userId) {
        Optional<Server> serverToUpdate = serverRepository.findById(request.getId());

        if (serverToUpdate.isEmpty()) {
            throw new RecordNotFoundException("Server not found");
        }

        if (!serverToUpdate.get().getOwnerId().equals(userId)) {
            throw new RecordNotFoundException("Server not found");
        }

        if (request.getName() != null && !request.getName().trim().equals(serverToUpdate.get().getName().trim())) {
            serverToUpdate.get().setName(request.getName());
        }

        if (request.getDescription() != null) {
            serverToUpdate.get().setDescription(request.getDescription());
        }

        if (request.getIconUrl() != null) {
            serverToUpdate.get().setIconUrl(request.getIconUrl());
        }

        serverToUpdate.get().setUpdatedAt(Instant.now());
        return serverRepository.save(serverToUpdate.get());
    }

    @Override
    public void delete(UUID id, UUID userId) {
        Optional<Server> serverToDelete = serverRepository.findById(id);

        if (serverToDelete.isEmpty()) {
            throw new RecordNotFoundException("Server not found");
        }

        if (!serverToDelete.get().getOwnerId().equals(userId)) {
            throw new RecordNotFoundException("Server not found");
        }

        serverToDelete.get().setDeletedAt(Instant.now());
        serverRepository.delete(serverToDelete.get());
    }

    @Override
    public Optional<Server> findByNameAndOwnerId(String name, UUID ownerId) {
        return serverRepository.findByNameAndOwnerId(name, ownerId);
    }

    @Override
    public List<Server> findByUserId(UUID userId) {
        return serverRepository.findByOwnerIdOrderByCreatedAtDesc(userId);
    }
}
