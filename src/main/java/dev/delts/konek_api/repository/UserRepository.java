package dev.delts.konek_api.repository;

import dev.delts.konek_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
   // TODO: Refactor this to use only one field
   Optional<User> findByEmailOrUserName(String email, String userName);

   Optional<User> findByEmail(String email);

   Optional<User> findByUserName(String userName);
}
