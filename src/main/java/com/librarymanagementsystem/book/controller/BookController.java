package com.librarymanagementsystem.book.controller;

import com.librarymanagementsystem.book.dto.BookCreateRequest;
import com.librarymanagementsystem.book.dto.BookResponse;
import com.librarymanagementsystem.book.dto.BookUpdateRequest;
import com.librarymanagementsystem.book.service.BookService;
import com.librarymanagementsystem.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {


    private final BookService bookService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookResponse>> createBook(@Valid @RequestBody BookCreateRequest bookCreateRequest) {

        BookResponse bookResponse = bookService.createBook(bookCreateRequest);

        return new ResponseEntity<>(ApiResponse.success("Book successfully created", bookResponse), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBookList() {

        List<BookResponse> bookResponse = bookService.getBookList();

        return new ResponseEntity<>(ApiResponse.success("The books were delivered successfully.", bookResponse), HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> getBookByID(@PathVariable Long id) {

        BookResponse bookResponse = bookService.getBookByID(id);

        return new ResponseEntity<>(ApiResponse.success("The book was delivered successfully.", bookResponse), HttpStatus.OK);
    }


    @GetMapping("/search/title")
    public ResponseEntity<ApiResponse<List<BookResponse>>> getBookByTitle(@RequestParam String title) {

        List<BookResponse> bookResponse = bookService.getBookByTitle(title);

        return new ResponseEntity<>(ApiResponse.success("The books were delivered successfully.", bookResponse), HttpStatus.OK);
    }
    @GetMapping("/search/isbn")
    public ResponseEntity<ApiResponse<BookResponse>> getBookByIsbn(@RequestParam String isbn) {

        BookResponse bookResponse = bookService.getBookByIsbn(isbn);

        return new ResponseEntity<>(ApiResponse.success("The books were delivered successfully.", bookResponse), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<BookResponse>> updateBook(@Valid @RequestBody BookUpdateRequest bookUpdateRequest,
                                                                @PathVariable Long id) {

        BookResponse bookResponse = bookService.updateBook(bookUpdateRequest, id);

        return new ResponseEntity<>(ApiResponse.success("Book successfully updated", bookResponse), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id) {

        String status = bookService.deleteBook(id);

        return new ResponseEntity<>(ApiResponse.success("Book successfully deleted", status), HttpStatus.OK);
    }


}
