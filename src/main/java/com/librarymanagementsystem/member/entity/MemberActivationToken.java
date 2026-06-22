package com.librarymanagementsystem.member.entity;

import com.librarymanagementsystem.common.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Entity
@Table(name = "member_activation_token")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberActivationToken extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activationTokenId;
    @ManyToOne
    @JoinColumn(name = "member_id", unique = true, nullable = false)
    private Member member;

    @Column(nullable = false)
    private String tokenHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false)
    private LocalDateTime usedAt;
}
