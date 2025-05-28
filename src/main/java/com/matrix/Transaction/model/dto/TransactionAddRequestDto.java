package com.matrix.Transaction.model.dto;

import com.matrix.Transaction.model.entity.TransactionType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class TransactionAddRequestDto {
    private Double amount;
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;
    private String accountNumber;
    private String username;
    private String password;
}
