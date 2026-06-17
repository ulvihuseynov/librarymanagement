package com.librarymanagementsystem.fine.entity;


import com.librarymanagementsystem.common.audit.BaseEntity;
import com.librarymanagementsystem.loan.entity.Loan;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "fines")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Fine extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fineId;

    @OneToOne
    @JoinColumn(name = "loan_id",nullable = false)
    private Loan loan;

    @Column(nullable = false)
    private Integer amount;

    @Column(nullable = false)
    private Integer daysLate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FineStatus status;

    @Column(nullable = false)
    private LocalDate calculatedAt;

    @Column(nullable = false)
    private Integer paidAt;

}
