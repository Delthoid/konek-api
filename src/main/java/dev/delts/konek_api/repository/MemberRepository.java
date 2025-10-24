package dev.delts.konek_api.repository;

import dev.delts.konek_api.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    Page<Member> findByServerId(UUID serverId, Pageable pageable);
    List<Member> findByRole(String role);
    Optional<Member> findByServerIdAndUserId(UUID serverId, UUID userId);
}
