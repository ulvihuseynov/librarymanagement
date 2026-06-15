package com.librarymanagementsystem.book.service.impl;

import com.librarymanagementsystem.book.dto.BookCreateRequest;
import com.librarymanagementsystem.book.dto.BookResponse;
import com.librarymanagementsystem.book.dto.BookUpdateRequest;
import com.librarymanagementsystem.book.entity.Book;
import com.librarymanagementsystem.book.mapper.BookMapper;
import com.librarymanagementsystem.book.repository.BookRepository;
import com.librarymanagementsystem.book.service.BookService;
import com.librarymanagementsystem.common.exception.DuplicateResourceException;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

    @Override
    public BookResponse createBook(BookCreateRequest bookCreateRequest) {
        Book book = bookMapper.toEntity(bookCreateRequest);

        boolean existsByIsbn = bookRepository.existsByIsbn(bookCreateRequest.getIsbn());

        if (existsByIsbn){
            throw new DuplicateResourceException("Book already exists "+bookCreateRequest.getIsbn());
        }
        book.setAvailableCopies(bookCreateRequest.getTotalCopies());

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public List<BookResponse> getBookList() {

        List<Book> bookList = bookRepository.findAll();
        return bookList.stream().map(bookMapper::toResponse).toList();
    }

    @Override
    public BookResponse getBookByID(Long id) {

        Book book = getBook(id);

        return bookMapper.toResponse(book);
    }

    @Override
    public BookResponse updateBook(BookUpdateRequest bookUpdateRequest, Long id) {

        Book bookFromDb = getBook(id);

        bookFromDb.setTitle(bookUpdateRequest.getTitle());
        bookFromDb.setDescription(bookUpdateRequest.getDescription());
        bookFromDb.setPublishedDate(bookUpdateRequest.getPublishedDate());

        // borrowing olanda yoxlama olacaq

        bookFromDb.setAvailableCopies(bookUpdateRequest.getTotalCopies());
        return bookMapper.toResponse(bookRepository.save(bookFromDb));
    }

    @Override
    public String deleteBook(Long id) {
        Book book = getBook(id);
        bookRepository.delete(book);
        return "Book successfully deleted with ID "+id;
    }

    @Override
    public List<BookResponse> getBookByTitle(String title) {
        List<Book> bookList = bookRepository.findByTitleContainingIgnoreCase(title);
        return bookList.stream().map(bookMapper::toResponse).toList();
    }

    @Override
    public BookResponse getBookByIsbn(String isbn) {

        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(()->new ResourceNotFoundException("Book not found with isbn "+isbn));

        return bookMapper.toResponse(book);
    }

    private Book getBook(Long id){
      return   bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID " + id));
    }
}
