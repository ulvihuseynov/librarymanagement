package com.librarymanagementsystem.member.dto;


import com.librarymanagementsystem.member.entity.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponse {


    private Long memberId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private MemberStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
