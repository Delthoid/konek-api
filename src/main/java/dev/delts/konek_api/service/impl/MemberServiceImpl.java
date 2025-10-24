package dev.delts.konek_api.service.impl;

import dev.delts.konek_api.dto.member.MemberResponseDto;
import dev.delts.konek_api.dto.member.UserMemberResponseDto;
import dev.delts.konek_api.dto.request.member.AddMemberRequest;
import dev.delts.konek_api.entity.Member;
import dev.delts.konek_api.entity.User;
import dev.delts.konek_api.exception.RecordNotFoundException;
import dev.delts.konek_api.repository.MemberRepository;
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

    public MemberServiceImpl(MemberRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public Member addMember(AddMemberRequest addMemberRequest) {
        Member newMember = new Member();
        newMember.setServerId(addMemberRequest.getServerId());
        newMember.setUserId(addMemberRequest.getUserId());
        newMember.setNickName("");

        return repository.save(newMember);
    }

    @Override
    public Page<MemberResponseDto> getMembers(UUID serverId, UUID userId, Pageable pageable) {
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
                            userDto,
                            member.getCreatedAt()
                    );
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(memberDtos, pageable, memberPage.getTotalElements());
    }
}
