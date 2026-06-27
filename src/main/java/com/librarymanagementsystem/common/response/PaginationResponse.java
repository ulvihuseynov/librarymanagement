package com.librarymanagementsystem.common.response;

import com.librarymanagementsystem.book.dto.BookResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaginationResponse<T> {

    private T data;
    private Integer pageSize;
    private Integer pageNumber;
    private Integer totalPages;
    private Long totalElements;
    private boolean isLast;


}
