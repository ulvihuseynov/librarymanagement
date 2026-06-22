package com.librarymanagementsystem.member.repository;

import com.librarymanagementsystem.member.entity.MemberActivationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberActivationTokenRepository extends JpaRepository<MemberActivationToken ,Long> {
}
