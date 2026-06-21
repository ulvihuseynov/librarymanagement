package com.librarymanagementsystem.member.service.impl;

import com.librarymanagementsystem.common.exception.DuplicateResourceException;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import com.librarymanagementsystem.member.dto.MemberCreateRequest;
import com.librarymanagementsystem.member.dto.MemberResponse;
import com.librarymanagementsystem.member.dto.MemberUpdateRequest;
import com.librarymanagementsystem.member.entity.Member;
import com.librarymanagementsystem.member.entity.MemberStatus;
import com.librarymanagementsystem.member.mapper.MemberMapper;
import com.librarymanagementsystem.member.repository.MemberRepository;
import com.librarymanagementsystem.member.service.MemberService;
import com.librarymanagementsystem.user.entity.AppRole;
import com.librarymanagementsystem.user.entity.Role;
import com.librarymanagementsystem.user.entity.User;
import com.librarymanagementsystem.user.repository.RoleRepository;
import com.librarymanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public MemberResponse createMember(MemberCreateRequest memberCreateRequest) {

        Member member = memberMapper.toEntity(memberCreateRequest);

        existsEmailValidation(memberCreateRequest.getEmail());

        existsPhoneNumberValidation(memberCreateRequest.getPhoneNumber());


        User user = createUser(memberCreateRequest);


        member.setUser(user);

        member.setStatus(MemberStatus.ACTIVE);

        return memberMapper.toResponse(memberRepository.save(member));
    }


    @Override
    public List<MemberResponse> getMemberList() {
        List<Member> memberList = memberRepository.findAll();
        return memberList.stream().map(memberMapper::toResponse).toList();
    }

    @Override
    public MemberResponse getMemberById(Long id) {

        Member member = getMember(id);

        return memberMapper.toResponse(member);

    }

    @Override
    public MemberResponse getMemberByEmail(String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with email " + email));
        return memberMapper.toResponse(member);
    }

    @Override
    public List<MemberResponse> getMemberByName(String name) {

        List<Member> memberList = memberRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        return memberList.stream().map(memberMapper::toResponse).toList();
    }

    @Override
    public MemberResponse updateMember(MemberUpdateRequest memberUpdateRequest, Long id) {

        Member memberFromDb = getMember(id);

        boolean existsByPhoneNumberAndMemberIdNot = memberRepository.existsByPhoneNumberAndMemberIdNot(memberUpdateRequest.getPhoneNumber(), id);
        if (existsByPhoneNumberAndMemberIdNot) {

            throw new DuplicateResourceException("Member already exists with phone number " + memberUpdateRequest.getPhoneNumber());
        }

        memberFromDb.setLastName(memberUpdateRequest.getLastName());
        memberFromDb.setFirstName(memberUpdateRequest.getFirstName());
        memberFromDb.setAddress(memberUpdateRequest.getAddress());
        memberFromDb.setPhoneNumber(memberUpdateRequest.getPhoneNumber());

        return memberMapper.toResponse(memberRepository.save(memberFromDb));
    }

    @Override
    public MemberResponse deActiveMember(Long id) {

        Member member = getMember(id);
        member.setStatus(MemberStatus.INACTIVE);

        return memberMapper.toResponse(memberRepository.save(member));
    }

    @Override
    public MemberResponse getCurrentMemberProfile() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        Member member=memberRepository.findByUserUsername(username)
                .orElseThrow(()->new ResourceNotFoundException("Member not found profile current user"));

        return memberMapper.toResponse(member);
    }

    private Member getMember(Long id) {

        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID " + id));
    }


    private User createUser(MemberCreateRequest memberCreateRequest) {

        User user = new User();

        Role role = roleRepository.findByRoleName(AppRole.ROLE_MEMBER)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found " + AppRole.ROLE_MEMBER));

        user.setUsername(memberCreateRequest.getUsername());
        user.setEnabled(true);
        user.setEmail(memberCreateRequest.getEmail());
        user.setPassword(passwordEncoder.encode(memberCreateRequest.getPassword()));
        user.setRoles(Set.of(role));

        userRepository.save(user);

        return user;
    }

    private void existsPhoneNumberValidation(String phoneNumber) {
        boolean existsPhoneNumber = memberRepository.existsByPhoneNumber(phoneNumber);

        if (existsPhoneNumber) {
            throw new DuplicateResourceException("Member already exists with phone number " + phoneNumber);
        }
    }

    private void existsEmailValidation(String email) {

        boolean existsEmail = memberRepository.existsByEmail(email);


        if (existsEmail) {
            throw new DuplicateResourceException("Member already exists with email " + email);
        }
    }
}
