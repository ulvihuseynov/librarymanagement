package com.librarymanagementsystem.member.service.impl;

import com.librarymanagementsystem.auth.service.ActivationService;
import com.librarymanagementsystem.common.exception.DuplicateResourceException;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import com.librarymanagementsystem.member.dto.ActivationTokenResult;
import com.librarymanagementsystem.member.dto.MemberCreateRequest;
import com.librarymanagementsystem.member.dto.MemberResponse;
import com.librarymanagementsystem.member.dto.MemberUpdateRequest;
import com.librarymanagementsystem.member.entity.Member;
import com.librarymanagementsystem.member.entity.MemberStatus;
import com.librarymanagementsystem.member.mapper.MemberMapper;
import com.librarymanagementsystem.member.repository.MemberRepository;
import com.librarymanagementsystem.member.service.MemberService;
import com.librarymanagementsystem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ActivationService activationService;
    private final MemberMapper memberMapper;


    @Transactional
    @Override
    public MemberResponse createMember(MemberCreateRequest memberCreateRequest) {

        Member member = memberMapper.toEntity(memberCreateRequest);

        existsEmailValidation(memberCreateRequest.getEmail());

        existsPhoneNumberValidation(memberCreateRequest.getPhoneNumber());


        member.setStatus(MemberStatus.ACTIVE);
        Member savedMember = memberRepository.save(member);
        ActivationTokenResult memberActivationToken = activationService.createMemberActivationToken(member);

        return memberMapper.toResponse(savedMember);
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

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        Member member = memberRepository.findByUserUserId(userDetails.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found profile current user"));

        return memberMapper.toResponse(member);
    }

    private Member getMember(Long id) {

        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID " + id));
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
