package com.matrix.Transaction.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@ToString
@Table(name = "transactions")
public class Transaction extends BaseEntity {
    private Double amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "account_id",nullable = false)
    private Account account;
}
