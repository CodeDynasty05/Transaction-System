package com.matrix.Transaction.controller;

import com.matrix.Transaction.model.dto.TransactionAddRequestDTO;
import com.matrix.Transaction.model.dto.TransactionDTO;
import com.matrix.Transaction.model.entity.TransactionStatus;
import com.matrix.Transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping
    private List<TransactionDTO> getTransactions(){
        return transactionService.getTransactions();
    }

    @GetMapping("/{accountNumber}")
    private List<TransactionDTO> getAccountTransactions(@PathVariable String accountNumber){
        return transactionService.getAccountTransactions(accountNumber);
    }

    @PostMapping
    private TransactionDTO createTransaction(@RequestBody TransactionAddRequestDTO transactionAddRequestDTO){
        return transactionService.addTransaction(transactionAddRequestDTO);
    }

    @PutMapping("/{transactionId}")
    private TransactionDTO changePaymentStatus(@PathVariable Long transactionId,@RequestParam TransactionStatus transactionStatus){
        return transactionService.changePaymentStatus(transactionId,transactionStatus);
    }
}
