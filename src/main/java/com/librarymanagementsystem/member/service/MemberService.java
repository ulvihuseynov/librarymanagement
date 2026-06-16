package com.librarymanagementsystem.member.service;

import com.librarymanagementsystem.member.dto.MemberCreateRequest;
import com.librarymanagementsystem.member.dto.MemberResponse;
import com.librarymanagementsystem.member.dto.MemberUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface MemberService {
    MemberResponse createMember(@Valid MemberCreateRequest memberCreateRequest);

    List<MemberResponse> getMemberList();

    MemberResponse getMemberById(Long id);

    MemberResponse getMemberByEmail(String email);

    List<MemberResponse> getMemberByName(String name);

    MemberResponse updateMember(@Valid MemberUpdateRequest memberUpdateRequest, Long id);

    MemberResponse deActiveMember(Long id);
}
