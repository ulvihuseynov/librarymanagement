package com.librarymanagementsystem.fine.mapper;

import com.librarymanagementsystem.fine.dto.FineResponse;
import com.librarymanagementsystem.fine.entity.Fine;
import com.librarymanagementsystem.loan.entity.Loan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FineMapper {


    @Mapping(target = "memberId",source = "loan.member.memberId")
    @Mapping(target = "bookId",source = "loan.book.bookId")
    @Mapping(target = "bookTitle",source = "loan.book.title")
@Mapping(target = "memberFullName",source = "loan",qualifiedByName = "mapMemberFullName")
    FineResponse toResponse(Fine fine);

    @Named("mapMemberFullName")
   default public String mapMemberFullName(Loan loan){

        if (loan ==null || loan.getMember()==null){
            return null;
        };
        return loan.getMember().getFirstName() + " " +loan.getMember().getLastName();
    }
}
