package com.librarymanagementsystem.book.service;

import com.librarymanagementsystem.book.dto.BookCreateRequest;
import com.librarymanagementsystem.book.dto.BookResponse;
import com.librarymanagementsystem.book.dto.BookUpdateRequest;
import jakarta.validation.Valid;

import java.util.List;

public interface BookService {

    BookResponse createBook(@Valid BookCreateRequest bookCreateRequest);

    List<BookResponse> getBookList();

    BookResponse getBookByID(Long id);

    BookResponse updateBook(@Valid BookUpdateRequest bookUpdateRequest, Long id);

    String deleteBook(Long id);

    List<BookResponse> getBookByTitle(String title);

    BookResponse getBookByIsbn(String isbn);
}
