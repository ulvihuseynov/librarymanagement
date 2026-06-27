package com.librarymanagementsystem.member.controller;

import com.librarymanagementsystem.common.response.ApiResponse;
import com.librarymanagementsystem.common.response.PaginationResponse;
import com.librarymanagementsystem.member.dto.MemberCreateRequest;
import com.librarymanagementsystem.member.dto.MemberResponse;
import com.librarymanagementsystem.member.dto.MemberUpdateRequest;
import com.librarymanagementsystem.member.service.MemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;


    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> createMember(@Valid @RequestBody MemberCreateRequest memberCreateRequest){

        MemberResponse memberResponse= memberService.createMember(memberCreateRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Member successfully created",memberResponse));
    }


    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<MemberResponse>>> getMemberList( @RequestParam (name = "pageSize",defaultValue = "10") Integer pageSize,
                                                                         @RequestParam (name = "pageNumber",defaultValue = "0") Integer pageNumber,
                                                                         @RequestParam (name = "sortBy",defaultValue = "firstName") String sortBy,
                                                                         @RequestParam (name = "sortDirection",defaultValue = "asc") String sortDirection){

        PaginationResponse<MemberResponse> memberResponse= memberService.getMemberList(pageSize,pageNumber,sortBy,sortDirection);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The members were delivered successfully.",memberResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberById(@PathVariable Long id){

        MemberResponse memberResponse= memberService.getMemberById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The member was delivered successfully.",memberResponse));
    }

    @GetMapping("/search/email")
    public ResponseEntity<ApiResponse<MemberResponse>> getMemberByEmail(@RequestParam String email){

        MemberResponse memberResponse= memberService.getMemberByEmail(email);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The member was delivered successfully.",memberResponse));
    }

    @GetMapping("/search/name")
    public ResponseEntity<ApiResponse<List<MemberResponse>>> getMemberByName(@RequestParam String name){

        List<MemberResponse> memberResponse= memberService.getMemberByName(name);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The member was delivered successfully.",memberResponse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> updateMember(@Valid @RequestBody MemberUpdateRequest memberUpdateRequest,
                                                                    @PathVariable Long id){

        MemberResponse memberResponse= memberService.updateMember(memberUpdateRequest,id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Member successfully update .",memberResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<MemberResponse>> deleteMember(@PathVariable Long id){

        MemberResponse memberResponse= memberService.deActiveMember(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("Member successfully deActive .",memberResponse));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<MemberResponse>> getCurrentMemberProfile(){

        MemberResponse memberResponse= memberService.getCurrentMemberProfile();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The current member profile was successfully delivered .",memberResponse));
    }


}
