package com.matrix.Transaction.controller;

import com.matrix.Transaction.model.dto.AccountDto;
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
    public List<AccountDto> getAccounts(@PathVariable Long id, @RequestParam(required = false) Boolean onlyActive) {
        return accountService.getAccounts(id,onlyActive);
    }

    @PostMapping("/{id}")
    public AccountDto createAccount(@PathVariable Long id) {
        return accountService.createAccount(id);
    }

    @PutMapping("/status/{accountNumber}")
    public AccountDto changeStatus(@PathVariable String accountNumber) {
        return accountService.changeStatus(accountNumber);
    }

    @DeleteMapping("/{accountNumber}")
    public void deleteAccount(@PathVariable String accountNumber) {
        accountService.deleteAccount(accountNumber);
    }
}
