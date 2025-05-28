package com.matrix.Transaction.service;

import com.matrix.Transaction.model.dto.AccountDto;
import com.matrix.Transaction.model.entity.Account;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AccountService {
    List<AccountDto> getAccounts(Long id, Boolean onlyActive);

    AccountDto createAccount(Long id);

    AccountDto changeStatus(String accountNumber);

    AccountDto increaseAccountBalance(Account account, Double amount);

    AccountDto decreaseAccountBalance(Account account, Double amount);

    void deleteAccount(String accountNumber);
}
