package com.matrix.Transaction.service;

import com.matrix.Transaction.model.dto.TransactionAddRequestDto;
import com.matrix.Transaction.model.dto.TransactionDto;
import com.matrix.Transaction.model.entity.TransactionStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {

    List<TransactionDto> getTransactions();

    List<TransactionDto> getAccountTransactions(String accountNumber);

    TransactionDto addTransaction(TransactionAddRequestDto transactionAddRequestDTO);

    TransactionDto changePaymentStatus(Long transactionId, TransactionStatus status);
}
