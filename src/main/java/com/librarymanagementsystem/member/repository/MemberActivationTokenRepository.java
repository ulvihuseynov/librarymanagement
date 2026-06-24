package com.librarymanagementsystem.member.repository;

import com.librarymanagementsystem.member.entity.MemberActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MemberActivationTokenRepository extends JpaRepository<MemberActivationToken ,Long> {


    Optional<MemberActivationToken> findByHashTokenAndExpiresAtGreaterThan(String hashToken, LocalDateTime now);
}
