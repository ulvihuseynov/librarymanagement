package com.librarymanagementsystem.member.mapper;

import com.librarymanagementsystem.member.dto.MemberCreateRequest;
import com.librarymanagementsystem.member.dto.MemberResponse;
import com.librarymanagementsystem.member.entity.Member;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MemberMapper {

    Member toEntity (MemberCreateRequest memberCreateRequest);

    MemberResponse toResponse(Member member);
}
