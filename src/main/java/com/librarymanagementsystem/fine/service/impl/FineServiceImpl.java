package com.librarymanagementsystem.fine.service.impl;


import com.librarymanagementsystem.common.exception.BadRequestException;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import com.librarymanagementsystem.common.response.PaginationResponse;
import com.librarymanagementsystem.fine.dto.FineResponse;
import com.librarymanagementsystem.fine.entity.Fine;
import com.librarymanagementsystem.fine.entity.FineStatus;
import com.librarymanagementsystem.fine.mapper.FineMapper;
import com.librarymanagementsystem.fine.repository.FineRepository;
import com.librarymanagementsystem.fine.service.FineService;
import com.librarymanagementsystem.loan.entity.LoanStatus;
import com.librarymanagementsystem.member.entity.Member;
import com.librarymanagementsystem.member.repository.MemberRepository;
import com.librarymanagementsystem.member.service.MemberAccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;
    private final MemberRepository memberRepository;
    private final FineMapper fineMapper;
    private final MemberAccessService memberAccessService;

    @Override
    public PaginationResponse<FineResponse> getFineList(Integer pageSize, Integer pageNumber, String sortBy, String sortDirection) {


        PaginationResponse<FineResponse> paginationResponse = new PaginationResponse<>();

        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Fine> fines = fineRepository.findAll(pageRequest);
        List<Fine> fineList = fines.getContent();
        List<FineResponse> fineResponses = fineList.stream().map(fineMapper::toResponse).toList();
        paginationResponse.setContent(fineResponses);

        paginationResponse.setPageSize(fines.getSize());
        paginationResponse.setPageNumber(fines.getNumber());
        paginationResponse.setTotalPages(fines.getTotalPages());
        paginationResponse.setTotalElements(fines.getTotalElements());
        paginationResponse.setLast(fines.isLast());

        return paginationResponse;
    }

    @Override
    public FineResponse getFineById(Long fineId) {
        Fine fine = getFine(fineId);
        return fineMapper.toResponse(fine);
    }

    @Override
    public List<FineResponse> getFineByMember(Long memberId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found with ID " + memberId));
        List<Fine> fineList = fineRepository.findByLoanMemberMemberId(memberId);
        return fineList.stream().map(fineMapper::toResponse).toList();
    }

    @Override
    public List<FineResponse> getFineByUnPaid() {

        List<Fine> fineList = fineRepository.findByStatus(FineStatus.UNPAID);
        return fineList.stream().map(fineMapper::toResponse).toList();

    }

    @Transactional
    @Override
    public FineResponse payFine(Long fineId) {

        Fine fineFromDb = getFine(fineId);
        if (fineFromDb.getLoan().getStatus() != LoanStatus.RETURNED) {
            throw new BadRequestException("Fine can be paid only after book is returned");
        }
        if (fineFromDb.getStatus() == FineStatus.PAID) {
            throw new BadRequestException("Fine already paid");
        }
        fineFromDb.setStatus(FineStatus.PAID);
        fineFromDb.setPaidAt(LocalDate.now());
        return fineMapper.toResponse(fineFromDb);
    }

    @Override
    public List<FineResponse> getMyFines() {

        Member currentMember = memberAccessService.getCurrentMember();
        List<Fine> fineList = fineRepository.findByLoanMemberMemberId(currentMember.getMemberId());
        return fineList.stream().map(fineMapper::toResponse).toList();
    }

    private Fine getFine(Long id) {
        return fineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fine not found with ID " + id));
    }
}
