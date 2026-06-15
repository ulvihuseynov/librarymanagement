package com.librarymanagementsystem.member.repository;

import com.librarymanagementsystem.member.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByEmail( String email);

    boolean existsByPhoneNumber(@NotBlank(message = "Phone number is required") String phoneNumber);

    boolean existsByPhoneNumberAndMemberIdNot( String phoneNumber, Long id);

    Member findByEmail(String email);

    List<Member> findByFirstName(String firstname);
}
