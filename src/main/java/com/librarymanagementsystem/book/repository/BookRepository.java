package com.librarymanagementsystem.book.repository;

import com.librarymanagementsystem.book.entity.Book;
import com.librarymanagementsystem.book.entity.BookStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);

    Page<Book> findByTitleContainingIgnoreCaseAndStatus(String title, BookStatus bookStatus,Pageable pageRequest);

    Optional<Book> findByIsbnAndStatus(String isbn, BookStatus bookStatus);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Book b where b.bookId= :bookId")
    Optional<Book> findByIdForUpdate(@Param("bookId") Long bookId);

    Page<Book> findByStatus(BookStatus bookStatus, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Book b where b.bookId= :bookId and b.status=:status")
    Optional<Book> findByIdForUpdateAndStatus(@Param("bookId") Long bookId, @Param("status") BookStatus status);

}
