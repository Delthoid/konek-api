package dev.delts.konek_api.service;

import dev.delts.konek_api.dto.member.MemberResponseDto;
import dev.delts.konek_api.dto.member.AddMemberRequest;
import dev.delts.konek_api.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MemberService {
    Member addMember(AddMemberRequest addMemberRequest, UUID serverId, UUID userId);
    Page<MemberResponseDto> getMembers(UUID serverId, UUID userId, Pageable pageable);
}
