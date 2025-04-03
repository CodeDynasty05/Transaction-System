package com.matrix.Transaction.service.impl;

import com.matrix.Transaction.exception.AccountNotFound;
import com.matrix.Transaction.exception.CustomerNotFound;
import com.matrix.Transaction.mapper.AccountMapper;
import com.matrix.Transaction.model.dto.AccountDTO;
import com.matrix.Transaction.model.entity.Account;
import com.matrix.Transaction.model.entity.AccountStatus;
import com.matrix.Transaction.model.entity.Customer;
import com.matrix.Transaction.repository.AccountRepository;
import com.matrix.Transaction.repository.CustomerRepository;
import com.matrix.Transaction.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerRepository customerRepository;

    @Override
    public List<AccountDTO> getAccounts(Long id,Boolean onlyActive) {
        List<Account> accountList = accountRepository.findByCustomerId(id);
        if (accountList.isEmpty()) {
            throw new AccountNotFound("No Account Found For Customer id: " + id);
        }
        List<AccountDTO> accounts = accountList.stream().map(accountMapper::accountToAccountDTO).collect(Collectors.toList());
        if(onlyActive){
            accounts = accounts.stream().filter(account -> account.getAccountStatus().equals(AccountStatus.ACTIVE)).collect(Collectors.toList());
        }
        if(accounts.isEmpty()){
            throw new AccountNotFound("Customer with id " + id + " doesn't have any account with given status filter.");
        }
        return accounts;
    }

    @Override
    public AccountDTO createAccount(Long id) {
        Account account = Account.builder().accountNumber(generateRandomCardNumber()).accountStatus(AccountStatus.INACTIVE).balance(100.0).customer(customerRepository.findById(id).orElseThrow(()->new CustomerNotFound("Customer with id " +id+"not found"))).build();
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public AccountDTO changeStatus(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFound("Account with id " + accountNumber + " not found");
        }
        if(account.getAccountStatus().equals(AccountStatus.ACTIVE)){
            account.setAccountStatus(AccountStatus.INACTIVE);
        } else if (account.getAccountStatus().equals(AccountStatus.INACTIVE)) {
            account.setAccountStatus(AccountStatus.ACTIVE);
        }
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public AccountDTO increaseAccountBalance(Account account, Double amount) {
        if(amount > 0 && account.getAccountStatus().equals(AccountStatus.ACTIVE)){
            account.setBalance(account.getBalance() + amount);
        }
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public AccountDTO decreaseAccountBalance(Account account, Double amount) {
        if(amount > 0  && account.getAccountStatus().equals(AccountStatus.ACTIVE) && account.getBalance()>=amount){
            account.setBalance(account.getBalance() - amount);
        }
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new AccountNotFound("Account with id " + accountNumber + " not found");
        }
        accountRepository.delete(account);
    }

    private static String generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 16; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }
}
