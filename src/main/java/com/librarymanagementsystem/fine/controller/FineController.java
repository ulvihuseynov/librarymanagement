package com.librarymanagementsystem.fine.controller;

import com.librarymanagementsystem.common.response.ApiResponse;
import com.librarymanagementsystem.common.response.PaginationResponse;
import com.librarymanagementsystem.fine.dto.FineResponse;
import com.librarymanagementsystem.fine.service.FineService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Fine")
@RestController
@RequestMapping("/api/fines")
@RequiredArgsConstructor
public class FineController {

    private final FineService fineService;


    @GetMapping
    public ResponseEntity<ApiResponse<PaginationResponse<FineResponse>>> getFineList( @RequestParam (name = "pageSize",defaultValue = "10") Integer pageSize,
                                                                                                   @RequestParam (name = "pageNumber",defaultValue = "0") Integer pageNumber,
                                                                                                   @RequestParam (name = "sortBy",defaultValue = "calculatedAt") String sortBy,
                                                                                                   @RequestParam (name = "sortDirection",defaultValue = "desc") String sortDirection){

        PaginationResponse<FineResponse> fineResponse= fineService.getFineList(pageSize,pageNumber,sortBy,sortDirection);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The fines were successfully delivered",fineResponse));
    }

    @GetMapping("/{fineId}")
    public ResponseEntity<ApiResponse<FineResponse>> getFineById(@PathVariable Long fineId){

        FineResponse fineResponse= fineService.getFineById(fineId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The fine was successfully delivered",fineResponse));
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<ApiResponse<List<FineResponse>>> getFineByMember(@PathVariable Long memberId){

        List<FineResponse> fineResponse= fineService.getFineByMember(memberId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The fines were successfully delivered",fineResponse));
    }

    @GetMapping("/unpaid")
    public ResponseEntity<ApiResponse<List<FineResponse>>> getFineByUnPaid(){

        List<FineResponse> fineResponse= fineService.getFineByUnPaid();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The fine was successfully delivered",fineResponse));
    }

    @PutMapping("/{fineId}/pay")
    public ResponseEntity<ApiResponse<FineResponse>> payFine(@PathVariable Long fineId){

        FineResponse fineResponse= fineService.payFine(fineId);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The fine was successfully updated",fineResponse));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<FineResponse>>> getMyFines(){

        List<FineResponse> fineResponse= fineService.getMyFines();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success("The fines were successfully  retrieved",fineResponse));
    }
}
