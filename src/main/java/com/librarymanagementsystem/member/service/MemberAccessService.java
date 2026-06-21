package com.librarymanagementsystem.member.service;

import com.librarymanagementsystem.common.exception.BadRequestException;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import com.librarymanagementsystem.loan.entity.Loan;
import com.librarymanagementsystem.member.entity.Member;
import com.librarymanagementsystem.member.repository.MemberRepository;
import com.librarymanagementsystem.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MemberAccessService {

    private final MemberRepository memberRepository;

    public void checkMemberCanAccessLoan(Loan loan){

        Member currentMember = getCurrentMember();
        Long currentMemberId = currentMember.getMemberId();
        Long memberId = loan.getMember().getMemberId();

        if (!currentMemberId.equals(memberId)){
            throw new BadRequestException("You are not allowed to access this loan");
        }
    }

    public Member getCurrentMember(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl)authentication.getPrincipal();

        if (userDetails ==null || userDetails.getUserId()==null){

            throw new BadRequestException("Authentication is required");
        }
        return memberRepository.findByUserUserId(userDetails.getUserId()).
                orElseThrow(() -> new ResourceNotFoundException("Member profile not found for current user "+ userDetails.getUserId()));
    }

   public boolean isAdmin(){

       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

       if (authentication==null){
           return false;
       }
     return   authentication.getAuthorities().stream().anyMatch(
             authority-> Objects.requireNonNull(authority.getAuthority()).equalsIgnoreCase("ROLE_ADMIN"));

   }
}
