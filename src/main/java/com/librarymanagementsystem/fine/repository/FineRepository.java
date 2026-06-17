package com.librarymanagementsystem.fine.repository;

import com.librarymanagementsystem.fine.entity.Fine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FineRepository extends JpaRepository<Fine,Long> {
}
