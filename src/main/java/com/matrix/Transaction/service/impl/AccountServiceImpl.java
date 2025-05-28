package com.matrix.Transaction.service.impl;

import com.matrix.Transaction.exception.AccountNotFoundException;
import com.matrix.Transaction.exception.CustomerNotFoundException;
import com.matrix.Transaction.exception.ResourceNotFoundException;
import com.matrix.Transaction.mapper.AccountMapper;
import com.matrix.Transaction.model.dto.AccountDto;
import com.matrix.Transaction.model.entity.Account;
import com.matrix.Transaction.model.entity.AccountStatus;
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
    public List<AccountDto> getAccounts(Long id, Boolean onlyActive) {
        List<Account> accountList = accountRepository.findByCustomerId(id);
        if (accountList.isEmpty()) {
            throw new AccountNotFoundException("No Account Found For Customer id: " + id);
        }
        List<AccountDto> accounts = accountList.stream().map(accountMapper::accountToAccountDTO).collect(Collectors.toList());
        if(onlyActive){
            accounts = accounts.stream().filter(account -> account.getAccountStatus().equals(AccountStatus.ACTIVE)).collect(Collectors.toList());
        }
        if(accounts.isEmpty()){
            throw new AccountNotFoundException("Customer with id " + id + " doesn't have any account with given status filter.");
        }
        return accounts;
    }

    @Override
    public AccountDto createAccount(Long id) {
        Account account = Account.builder().accountNumber(generateRandomCardNumber()).accountStatus(AccountStatus.INACTIVE).balance(100.0).customer(customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer with id " +id+"not found"))).build();
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public AccountDto changeStatus(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()->new AccountNotFoundException("Account with id " + accountNumber + " not found"));

        if(account.getAccountStatus().equals(AccountStatus.ACTIVE)){
            account.setAccountStatus(AccountStatus.INACTIVE);
        } else if (account.getAccountStatus().equals(AccountStatus.INACTIVE)) {
            account.setAccountStatus(AccountStatus.ACTIVE);
        }
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public AccountDto increaseAccountBalance(Account account, Double amount) {
        if(amount > 0 && account.getAccountStatus().equals(AccountStatus.ACTIVE)){
            account.setBalance(account.getBalance() + amount);
        }
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public AccountDto decreaseAccountBalance(Account account, Double amount) {
        if(amount > 0  && account.getAccountStatus().equals(AccountStatus.ACTIVE) && account.getBalance()>=amount){
            account.setBalance(account.getBalance() - amount);
        } else{
            throw new ResourceNotFoundException("Account with number " + account.getAccountNumber() + " doesn't have enough balance.");
        }
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public void deleteAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()->new AccountNotFoundException("Account with id " + accountNumber + " not found"));
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
