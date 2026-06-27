package com.librarymanagementsystem.common.response;


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

    private Integer pageSize;

    private Integer pageNumber;
    private Integer totalPages;
    private Long totalElements;
    private boolean isLast;


}
