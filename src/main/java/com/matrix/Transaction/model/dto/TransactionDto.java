package com.matrix.Transaction.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.matrix.Transaction.model.entity.Account;
import com.matrix.Transaction.model.entity.TransactionStatus;
import com.matrix.Transaction.model.entity.TransactionType;
import lombok.Data;

@Data
public class TransactionDto {
    private Long id;
    private Double amount;
    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    @JsonBackReference
    private Account account;
}
