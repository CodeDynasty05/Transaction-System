package com.matrix.Transaction.service.impl;

import com.matrix.Transaction.exception.AccountInactive;
import com.matrix.Transaction.exception.Forbidden;
import com.matrix.Transaction.exception.StatusNotSuitable;
import com.matrix.Transaction.exception.TransactionNotFound;
import com.matrix.Transaction.mapper.TransactionMapper;
import com.matrix.Transaction.model.dto.TransactionAddRequestDTO;
import com.matrix.Transaction.model.dto.TransactionDTO;
import com.matrix.Transaction.model.entity.*;
import com.matrix.Transaction.repository.AccountRepository;
import com.matrix.Transaction.repository.TransactionRepository;
import com.matrix.Transaction.service.AccountService;
import com.matrix.Transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @Override
    public List<TransactionDTO> getTransactions() {
        return transactionRepository.findAll().stream().map(transactionMapper::transactionToTransactionDTO).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getAccountTransactions(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new TransactionNotFound("Not found any transaction with account number: " + accountNumber);
        }
        return transactionRepository.findByAccountId(account.getId()).stream().map(transactionMapper::transactionToTransactionDTO).collect(Collectors.toList());
    }

    @Override
    public TransactionDTO addTransaction(TransactionAddRequestDTO transactionAddRequestDTO) {
        Transaction transaction = transactionMapper.transactionAddRequestDTOtoTransaction(transactionAddRequestDTO);
        if (transaction.getAccount().getAccountStatus() == AccountStatus.ACTIVE) {
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            return transactionMapper.transactionToTransactionDTO(transactionRepository.save(transaction));
        }else {
            throw new AccountInactive("Account with account number: " + transactionAddRequestDTO.getAccountNumber() + " is not active");
        }
    }

    @Override
    public TransactionDTO changePaymentStatus(Long transactionId, TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(()->new TransactionNotFound("Transaction Not Found With Id: "+transactionId));
        if(transaction.getAccount().getAccountStatus() != AccountStatus.ACTIVE) {
            throw new AccountInactive("The account is not active");
        }
        if(status == TransactionStatus.SUCCESS
           && transaction.getTransactionStatus() == TransactionStatus.PENDING){
            switch (transaction.getTransactionType()){
                case TransactionType.PURCHASE:
                    accountService.decreaseAccountBalance(transaction.getAccount(),transaction.getAmount());
                    break;
                case TransactionType.DEPOSIT:
                    accountService.increaseAccountBalance(transaction.getAccount(),transaction.getAmount());
                    break;
                case TransactionType.WITHDRAWAL:
                    accountService.decreaseAccountBalance(transaction.getAccount(),transaction.getAmount());
                    break;
            }
            transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        } else if (status == TransactionStatus.REFUNDED
                && transaction.getTransactionStatus() == TransactionStatus.SUCCESS
                && transaction.getTransactionType() == TransactionType.PURCHASE) {
            accountService.increaseAccountBalance(transaction.getAccount(),transaction.getAmount());
            transaction.setTransactionStatus(TransactionStatus.REFUNDED);
        } else if (status==TransactionStatus.FAILED
                    && transaction.getTransactionStatus() == TransactionStatus.PENDING
        ){
            transaction.setTransactionStatus(status);
        } else {
            throw new StatusNotSuitable("The process is not possible with current status");
        }
        return transactionMapper.transactionToTransactionDTO(transactionRepository.save(transaction));
    }
}
