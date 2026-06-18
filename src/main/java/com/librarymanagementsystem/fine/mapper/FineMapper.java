package com.librarymanagementsystem.fine.mapper;

import com.librarymanagementsystem.fine.dto.FineResponse;
import com.librarymanagementsystem.fine.entity.Fine;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FineMapper {


    FineResponse toResponse(Fine fine);
}
