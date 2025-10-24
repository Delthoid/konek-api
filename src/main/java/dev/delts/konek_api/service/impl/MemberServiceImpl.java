package dev.delts.konek_api.service.impl;

import dev.delts.konek_api.dto.member.MemberResponseDto;
import dev.delts.konek_api.dto.member.UserMemberResponseDto;
import dev.delts.konek_api.dto.member.AddMemberRequest;
import dev.delts.konek_api.entity.Member;
import dev.delts.konek_api.entity.Server;
import dev.delts.konek_api.entity.User;
import dev.delts.konek_api.exception.RecordAlreadyExistsException;
import dev.delts.konek_api.exception.RecordNotFoundException;
import dev.delts.konek_api.repository.MemberRepository;
import dev.delts.konek_api.repository.ServerRepository;
import dev.delts.konek_api.repository.UserRepository;
import dev.delts.konek_api.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository repository;
    private final UserRepository userRepository;
    private final ServerRepository serverRepository;

    public MemberServiceImpl(MemberRepository repository, UserRepository userRepository, ServerRepository serverRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
        this.serverRepository = serverRepository;
    }

    @Override
    public Member addMember(AddMemberRequest addMemberRequest, UUID serverId, UUID userId) {
        // Check first if the current user is a member of the server
        Optional<Member> membership = repository.findByServerIdAndUserId(serverId, userId);

        if (membership.isEmpty()) {
            throw new RecordNotFoundException("Server not found.");
        }

        // Check if the server is active or not deleted
        Optional<Server> server = serverRepository.findById(serverId);
        if (server.isEmpty()) {
            throw new RecordNotFoundException("Server not found");
        }

        // Check if member request user already a member
        Optional<Member> existingMembership = repository.findByServerIdAndUserId(serverId, addMemberRequest.getUserId());
        if (existingMembership.isPresent()) {
            throw new RecordAlreadyExistsException("User is already a member of this server.");
        }

        Member newMember = new Member();
        newMember.setServerId(serverId);
        newMember.setUserId(addMemberRequest.getUserId());
        newMember.setNickName("");
        newMember.setAddedBy(userId);

        return repository.save(newMember);
    }

    @Override
    public Page<MemberResponseDto> getMembers(UUID serverId, UUID userId, Pageable pageable) {
        // Check first if the current user is a member of the server
        Optional<Member> membership = repository.findByServerIdAndUserId(serverId, userId);

        if (membership.isEmpty()) {
            throw new RecordNotFoundException("Server not found");
        }

        // Get the members first
        Page<Member> memberPage = repository.findByServerId(serverId, pageable);

        List<Member> membersOnPage = memberPage.getContent();
        if (membersOnPage.isEmpty()) {
            return Page.empty(pageable);
        }

        // Get userId from each member
        List<UUID> userIds = membersOnPage.stream()
                .map(Member::getUserId)
                .distinct()
                .collect(Collectors.toList());

        // And then get each member userDetails
        Map<UUID, User> userMap = userRepository.findAllById(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        List<MemberResponseDto> memberDtos = membersOnPage
                .stream()
                .map(member -> {
                    User user = userMap.get(member.getUserId());
                    if (user == null) {
                        return null;
                    }

                    UserMemberResponseDto userDto = new UserMemberResponseDto(
                            user.getId(),
                            user.getUserName(),
                            user.getAvatarUrl()
                    );

                    return new MemberResponseDto(
                            member.getRole(),
                            member.getNickName(),
                            userDto,
                            member.getCreatedAt()
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(memberDtos, pageable, memberPage.getTotalElements());
    }
}
