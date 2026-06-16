package com.librarymanagementsystem.loan;

import com.librarymanagementsystem.loan.dto.LoanCreateRequest;
import com.librarymanagementsystem.loan.dto.LoanResponse;
import com.librarymanagementsystem.loan.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy =  ReportingPolicy.IGNORE)
public interface LoanMapper {

    Loan toEntity(LoanCreateRequest loanCreateRequest);

    LoanResponse toResponse(Loan loan);
}
