package com.librarymanagementsystem.reservation.mapper;

import com.librarymanagementsystem.member.entity.Member;
import com.librarymanagementsystem.reservation.dto.ReservationCreateRequest;
import com.librarymanagementsystem.reservation.dto.ReservationResponse;
import com.librarymanagementsystem.reservation.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReservationMapper {

    Reservation toEntity(ReservationCreateRequest reservationCreateRequest);


    @Mapping(target = "memberId",source = "member.memberId")
    @Mapping(target = "bookId",source = "book.bookId")
    @Mapping(target = "bookTitle",source = "book.title")
    @Mapping(target = "memberFullName",source = "member",qualifiedByName = "mapMemberFullName")
    ReservationResponse toResponse(Reservation reservation);

    @Named("mapMemberFullName")
    default String mapMemberFullName(Member member){
        if (member==null){
            return null;
        }
        return member.getFirstName()+ " "+member.getLastName();
    }
}
