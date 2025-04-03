package com.matrix.Transaction.service;

import com.matrix.Transaction.model.dto.AccountDTO;
import com.matrix.Transaction.model.entity.Account;
import com.matrix.Transaction.model.entity.AccountStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public interface AccountService {
    List<AccountDTO> getAccounts(Long id,Boolean onlyActive);

    AccountDTO createAccount(Long id);

    AccountDTO changeStatus(String accountNumber);

    AccountDTO increaseAccountBalance(Account account, Double amount);

    AccountDTO decreaseAccountBalance(Account account, Double amount);

    void deleteAccount(String accountNumber);
}
