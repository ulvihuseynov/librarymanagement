package com.librarymanagementsystem.book.repository;

import com.librarymanagementsystem.book.entity.Book;
import com.librarymanagementsystem.book.entity.BookStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book,Long> {

    boolean existsByIsbn(String isbn);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByTitleContainingIgnoreCase(String title);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Book b where b.bookId= :bookId")
    Optional<Book>  findByIdForUpdate(@Param("bookId") Long bookId);

    List<Book> findByStatus(BookStatus bookStatus);
}
