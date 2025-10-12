package dev.delts.konek_api.repository;

import dev.delts.konek_api.entity.Server;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServerRepository extends JpaRepository<Server, UUID> {
    Optional<Server> findByNameAndOwnerId(String name, UUID userId);
    Optional<Server> findByIdAndOwnerId(UUID id, UUID userId);
}
