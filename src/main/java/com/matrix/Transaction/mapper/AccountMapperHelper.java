package com.matrix.Transaction.mapper;

import com.matrix.Transaction.exception.AccountNotFoundException;
import com.matrix.Transaction.model.entity.Account;
import com.matrix.Transaction.repository.AccountRepository;
import org.springframework.stereotype.Component;

@Component
public class AccountMapperHelper {

    private static AccountRepository accountRepository;

    public AccountMapperHelper(AccountRepository accountRepository) {
        AccountMapperHelper.accountRepository = accountRepository;
    }

    public static Account mapAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(()->new AccountNotFoundException("No Account Found For Account Number: " + accountNumber));
    }
}
