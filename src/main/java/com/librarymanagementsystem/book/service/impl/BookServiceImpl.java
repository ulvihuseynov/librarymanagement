package com.librarymanagementsystem.book.service.impl;

import com.librarymanagementsystem.book.dto.BookCreateRequest;
import com.librarymanagementsystem.book.dto.BookResponse;
import com.librarymanagementsystem.book.dto.BookUpdateRequest;
import com.librarymanagementsystem.book.entity.Book;
import com.librarymanagementsystem.book.entity.BookStatus;
import com.librarymanagementsystem.book.mapper.BookMapper;
import com.librarymanagementsystem.book.repository.BookRepository;
import com.librarymanagementsystem.book.service.BookService;
import com.librarymanagementsystem.common.exception.BadRequestException;
import com.librarymanagementsystem.common.exception.DuplicateResourceException;
import com.librarymanagementsystem.common.exception.ResourceNotFoundException;
import com.librarymanagementsystem.common.response.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (existsByIsbn) {
            throw new DuplicateResourceException("Book already exists " + bookCreateRequest.getIsbn());
        }
        book.setAvailableCopies(bookCreateRequest.getTotalCopies());
        book.setStatus(BookStatus.ACTIVE);

        return bookMapper.toResponse(bookRepository.save(book));
    }

    @Override
    public PaginationResponse<BookResponse> getBookList(Integer pageSize, Integer pageNumber, String sortBy, String sortDirection) {


        PaginationResponse<BookResponse> paginationResponse=new PaginationResponse<>();

        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);

        Page<Book> books = bookRepository.findByStatus(BookStatus.ACTIVE, pageRequest);

        List<Book> bookList = books.getContent();

        List<BookResponse> bookResponses = bookList.stream().map(bookMapper::toResponse).toList();

        paginationResponse.setContent(bookResponses);

        paginationResponse.setPageSize(books.getSize());
        paginationResponse.setPageNumber(books.getNumber());
        paginationResponse.setTotalPages(books.getTotalPages());
        paginationResponse.setTotalElements(books.getTotalElements());
        paginationResponse.setLast(books.isLast());

        return paginationResponse;
    }

    @Override
    public BookResponse getBookByID(Long id) {

        Book book = getBook(id);

        return bookMapper.toResponse(book);
    }

    @Transactional
    @Override
    public BookResponse updateBook(BookUpdateRequest bookUpdateRequest, Long id) {

        Book bookFromDb = bookRepository.findByIdForUpdateAndStatus(id, BookStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID " + id));


        bookFromDb.setTitle(bookUpdateRequest.getTitle());
        bookFromDb.setDescription(bookUpdateRequest.getDescription());
        bookFromDb.setPublishedDate(bookUpdateRequest.getPublishedDate());

        int borrowedCopies = bookFromDb.getTotalCopies() - bookFromDb.getAvailableCopies();

        if (borrowedCopies > bookUpdateRequest.getTotalCopies()) {
            throw new BadRequestException("New total copies cannot be less than borrowed copies");
        }
        int newAvailableCopies = bookUpdateRequest.getTotalCopies() - borrowedCopies;

        bookFromDb.setTotalCopies(bookUpdateRequest.getTotalCopies());
        bookFromDb.setAvailableCopies(newAvailableCopies);

        return bookMapper.toResponse(bookRepository.save(bookFromDb));
    }

    @Transactional
    @Override
    public String deleteBook(Long id) {
        Book book = bookRepository.findByIdForUpdate(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID " + id));

        book.setStatus(BookStatus.ARCHIVED);
        bookRepository.save(book);
        return "Book successfully archived with ID " + id;
    }

    @Override
    public PaginationResponse<BookResponse> getBookByTitle(String title,Integer pageSize,Integer pageNumber,String sortBy,String sortDirection) {

        PaginationResponse<BookResponse> paginationResponse=new PaginationResponse<>();

        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        Pageable pageRequest = PageRequest.of(pageNumber, pageSize, sort);


        Page<Book> books = bookRepository.findByTitleContainingIgnoreCaseAndStatus(title, BookStatus.ACTIVE,pageRequest);

        List<Book> bookList = books.getContent();

        List<BookResponse> bookResponses = bookList.stream().map(bookMapper::toResponse).toList();

        paginationResponse.setContent(bookResponses);

        paginationResponse.setPageSize(books.getSize());
        paginationResponse.setPageNumber(books.getNumber());
        paginationResponse.setTotalPages(books.getTotalPages());
        paginationResponse.setTotalElements(books.getTotalElements());
        paginationResponse.setLast(books.isLast());

        return paginationResponse;
    }

    @Override
    public BookResponse getBookByIsbn(String isbn) {

        Book book = bookRepository.findByIsbnAndStatus(isbn, BookStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with isbn " + isbn));

        return bookMapper.toResponse(book);
    }

    private Book getBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID " + id));
    }
}
