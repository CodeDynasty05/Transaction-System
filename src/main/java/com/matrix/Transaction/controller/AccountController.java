package com.matrix.Transaction.controller;

import com.matrix.Transaction.model.dto.AccountDTO;
import com.matrix.Transaction.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/{id}")
    public List<AccountDTO> getAccounts(@PathVariable Long id,@RequestParam(required = false) Boolean isActive) {
        return accountService.getAccounts(id,isActive);
    }

    @PostMapping("/{id}")
    public AccountDTO createAccount(@PathVariable Long id) {
        return accountService.createAccount(id);
    }

    @PutMapping("/status/{accountNumber}")
    public AccountDTO changeStatus(@PathVariable String accountNumber) {
        return accountService.changeStatus(accountNumber);
    }

    @DeleteMapping("/{accountNumber}")
    public void deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
    }
}
