package com.librarymanagementsystem.fine.service.impl;

import com.librarymanagementsystem.book.entity.Book;
import com.librarymanagementsystem.book.repository.BookRepository;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import com.librarymanagementsystem.fine.dto.FineResponse;
import com.librarymanagementsystem.fine.entity.Fine;
import com.librarymanagementsystem.fine.entity.FineStatus;
import com.librarymanagementsystem.fine.mapper.FineMapper;
import com.librarymanagementsystem.fine.repository.FineRepository;
import com.librarymanagementsystem.fine.service.FineService;
import com.librarymanagementsystem.loan.entity.LoanStatus;
import com.librarymanagementsystem.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FineServiceImpl implements FineService {

    private final FineRepository fineRepository;
    private final MemberRepository memberRepository;
    private final BookRepository bookRepository;
    private final FineMapper fineMapper;

    @Override
    public List<FineResponse> getFineList() {
        List<Fine> fineList = fineRepository.findAll();
        return fineList.stream().map(fineMapper::toResponse).toList();
    }

    @Override
    public FineResponse getFineById(Long fineId) {
        Fine fine = getFine(fineId);
        return fineMapper.toResponse(fine);
    }

    @Override
    public List<FineResponse> getFineByMember(Long memberId) {

        memberRepository.findById(memberId)
                .orElseThrow(()->new ResourceNotFoundException("Member not found with ID "+memberId));
        List<Fine> fineList=fineRepository.findByLoanMemberMemberId(memberId);
        return fineList.stream().map(fineMapper::toResponse).toList();
    }

    @Override
    public List<FineResponse> getFineByUnPaid() {

        List<Fine> fineList=   fineRepository.findByStatusIn(List.of(FineStatus.UNPAID));
        return fineList.stream().map(fineMapper::toResponse).toList();

    }

    @Override
    public FineResponse getFinePaid(Long fineId) {

        Fine fineFromDb = getFine(fineId);

if (fineFromDb.getStatus()==FineStatus.UNPAID){
    fineFromDb.setStatus(FineStatus.PAID);
}
    return fineMapper.toResponse(fineFromDb);
    }

    private Fine getFine(Long id){
        return fineRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Fine not found with ID "+id));
    }
}
