package com.librarymanagementsystem.book.service;

import com.librarymanagementsystem.book.dto.BookCreateRequest;
import com.librarymanagementsystem.book.dto.BookResponse;
import com.librarymanagementsystem.book.dto.BookUpdateRequest;
import com.librarymanagementsystem.common.response.PaginationResponse;
import jakarta.validation.Valid;

import java.util.List;

public interface BookService {

    BookResponse createBook(@Valid BookCreateRequest bookCreateRequest);

    PaginationResponse<BookResponse> getBookList(Integer pageSize, Integer pageNumber, String sortBy, String sortDirection);


    BookResponse getBookByID(Long id);

    BookResponse updateBook(@Valid BookUpdateRequest bookUpdateRequest, Long id);

    String deleteBook(Long id);

    PaginationResponse<BookResponse> getBookByTitle(String title,Integer pageSize,Integer pageNumber,String sortBy,String sortDirection);

    BookResponse getBookByIsbn(String isbn);

}
