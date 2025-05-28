package com.matrix.Transaction.controller;

import com.matrix.Transaction.model.dto.TransactionAddRequestDto;
import com.matrix.Transaction.model.dto.TransactionDto;
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
    private List<TransactionDto> getTransactions(){
        return transactionService.getTransactions();
    }

    @GetMapping("/{accountNumber}")
    private List<TransactionDto> getAccountTransactions(@PathVariable String accountNumber){
        return transactionService.getAccountTransactions(accountNumber);
    }

    @PostMapping
    private TransactionDto createTransaction(@RequestBody TransactionAddRequestDto transactionAddRequestDTO){
        return transactionService.addTransaction(transactionAddRequestDTO);
    }

    @PutMapping("/{transactionId}")
    private TransactionDto changePaymentStatus(@PathVariable Long transactionId, @RequestParam TransactionStatus transactionStatus){
        return transactionService.changePaymentStatus(transactionId,transactionStatus);
    }
}
