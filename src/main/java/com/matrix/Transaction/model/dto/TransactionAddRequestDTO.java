package com.matrix.Transaction.model.dto;

import com.matrix.Transaction.model.entity.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class TransactionAddRequestDTO {
    private Double amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String accountNumber;
}
