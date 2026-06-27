package com.librarymanagementsystem.book.dto;

import com.librarymanagementsystem.common.response.PaginationResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {


    private Long bookId;
    private String title;
    private String isbn;
    private String description;
    private Integer totalCopies;
    private Integer availableCopies;
    private LocalDate publishedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;



}
