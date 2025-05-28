package com.matrix.Transaction.service.impl;

import com.matrix.Transaction.exception.AccountInactiveException;
import com.matrix.Transaction.exception.StatusNotSuitableException;
import com.matrix.Transaction.exception.TransactionNotFoundException;
import com.matrix.Transaction.mapper.TransactionMapper;
import com.matrix.Transaction.model.dto.TransactionAddRequestDto;
import com.matrix.Transaction.model.dto.TransactionDto;
import com.matrix.Transaction.model.entity.*;
import com.matrix.Transaction.repository.AccountRepository;
import com.matrix.Transaction.repository.CustomerRepository;
import com.matrix.Transaction.repository.TransactionRepository;
import com.matrix.Transaction.service.AccountService;
import com.matrix.Transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public List<TransactionDto> getTransactions() {
        return transactionRepository.findAll().stream().map(transactionMapper::transactionToTransactionDTO).collect(Collectors.toList());
    }

    @Override
    public List<TransactionDto> getAccountTransactions(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()->new TransactionNotFoundException("Not found any transaction with account number: " + accountNumber));
        List<Transaction> transactionList = transactionRepository.findByAccountId(account.getId());
        if (transactionList.isEmpty()) {
            throw new TransactionNotFoundException("Not found any transaction with account number: " + accountNumber);
        }
        return transactionList.stream().map(transactionMapper::transactionToTransactionDTO).collect(Collectors.toList());
    }

    @Override
    public TransactionDto addTransaction(TransactionAddRequestDto transactionAddRequestDTO) {
        Customer customer = customerRepository.findByUsername(transactionAddRequestDTO.getUsername())
                .orElseThrow(()->new TransactionNotFoundException("Customer with username: " + transactionAddRequestDTO.getUsername() + " not found"));
        if(!encoder.matches(transactionAddRequestDTO.getPassword(),customer.getPassword())){
            throw new TransactionNotFoundException("Password is not correct");
        }
        Transaction transaction = transactionMapper.transactionAddRequestDTOtoTransaction(transactionAddRequestDTO);
        if (transaction.getAccount().getAccountStatus() == AccountStatus.ACTIVE) {
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            return transactionMapper.transactionToTransactionDTO(transactionRepository.save(transaction));
        }else {
            throw new AccountInactiveException("Account with account number: " + transactionAddRequestDTO.getAccountNumber() + " is not active");
        }
    }

    @Override
    public TransactionDto changePaymentStatus(Long transactionId, TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(transactionId).orElseThrow(()->new TransactionNotFoundException("Transaction Not Found With Id: "+transactionId));
        if(transaction.getAccount().getAccountStatus() != AccountStatus.ACTIVE) {
            throw new AccountInactiveException("The account is not active");
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
            throw new StatusNotSuitableException("The process is not possible with current status");
        }
        return transactionMapper.transactionToTransactionDTO(transactionRepository.save(transaction));
    }
}
