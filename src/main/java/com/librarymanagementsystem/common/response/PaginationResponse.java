package com.librarymanagementsystem.common.response;

import com.librarymanagementsystem.book.dto.BookResponse;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {

    private List<T> content;

    @Min(value = 0,message = "Page size cannot negative")
    private Integer pageSize;

    @Min(value = 0,message = "Page number cannot negative")
    private Integer pageNumber;
    private Integer totalPages;
    private Long totalElements;
    private boolean isLast;


}
