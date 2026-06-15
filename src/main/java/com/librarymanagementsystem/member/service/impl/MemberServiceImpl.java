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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    @Override
    public MemberResponse createMember(MemberCreateRequest memberCreateRequest) {

        Member member = memberMapper.toEntity(memberCreateRequest);

        boolean existsEmail = memberRepository.existsByEmail(memberCreateRequest.getEmail());

        boolean existsPhoneNumber = memberRepository.existsByPhoneNumber(memberCreateRequest.getPhoneNumber());

        if (existsEmail) {
            throw new DuplicateResourceException("Member already exists with email " + memberCreateRequest.getEmail());
        }

        if (existsPhoneNumber) {
            throw new DuplicateResourceException("Member already exists with phone number " + memberCreateRequest.getPhoneNumber());
        }

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

        Member member=memberRepository.findByEmail(email);
        return memberMapper.toResponse(member);
    }

    @Override
    public List<MemberResponse> getMemberByFirstname(String firstname) {

        List<Member> memberList=memberRepository.findByFirstName(firstname);
        return memberList.stream().map(memberMapper::toResponse).toList();
    }

    @Override
    public MemberResponse updateMember(MemberUpdateRequest memberUpdateRequest, Long id) {

        Member memberFromDb = getMember(id);

        boolean existsByPhoneNumberAndMemberIdNot=memberRepository.existsByPhoneNumberAndMemberIdNot(memberUpdateRequest.getPhoneNumber(),id);
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

    private Member getMember(Long id){

       return memberRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Member not found with ID "+id));
    }
}
