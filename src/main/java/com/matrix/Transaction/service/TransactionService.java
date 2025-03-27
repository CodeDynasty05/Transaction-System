package com.matrix.Transaction.service;

import com.matrix.Transaction.model.dto.TransactionAddRequestDTO;
import com.matrix.Transaction.model.dto.TransactionDTO;
import com.matrix.Transaction.model.entity.TransactionStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {

    List<TransactionDTO> getTransactions();

    List<TransactionDTO> getAccountTransactions(String accountNumber);

    TransactionDTO addTransaction(TransactionAddRequestDTO transactionAddRequestDTO);

    TransactionDTO changePaymentStatus(Long transactionId, TransactionStatus status);
}
