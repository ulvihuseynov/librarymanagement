package com.librarymanagementsystem.member.entity;


import com.librarymanagementsystem.common.audit.BaseEntity;
import com.librarymanagementsystem.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "members")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false,unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberStatus status;


    @OneToOne
    @JoinColumn(name="user_id",nullable = false,unique = true)
    private User user;

}
