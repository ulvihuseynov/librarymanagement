package com.librarymanagementsystem.loan;

import com.librarymanagementsystem.loan.dto.LoanCreateRequest;
import com.librarymanagementsystem.loan.dto.LoanResponse;
import com.librarymanagementsystem.loan.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy =  ReportingPolicy.IGNORE)
public interface LoanMapper {

    Loan toEntity(LoanCreateRequest loanCreateRequest);

    @Mapping(target = "bookId",source = "book.bookId")
    @Mapping(target = "bookTitle",source = "book.title")
    @Mapping(target = "memberId",source = "member.memberId")
    @Mapping(target = "memberFullName",source = "loan",qualifiedByName = "mapMemberFullName")

    LoanResponse toResponse(Loan loan);

    @Named("mapMemberFullName")
    default String mapMemberFullName(Loan loan){
        if (loan ==null ||  loan.getMember()==null){
            return null;
        }
        return loan.getMember().getFirstName() + " " + loan.getMember().getLastName();
    }
}
