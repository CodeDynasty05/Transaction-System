package com.matrix.Transaction.service.impl;

import com.matrix.Transaction.mapper.AccountMapper;
import com.matrix.Transaction.model.dto.AccountDTO;
import com.matrix.Transaction.model.entity.Account;
import com.matrix.Transaction.model.entity.AccountStatus;
import com.matrix.Transaction.repository.AccountRepository;
import com.matrix.Transaction.repository.CustomerRepository;
import com.matrix.Transaction.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerRepository customerRepository;

    @Override
    public List<AccountDTO> getAccounts(Long id,Boolean isActive) {
        List<AccountDTO> accounts = accountRepository.findByCustomerId(id).stream().map(accountMapper::accountToAccountDTO).collect(Collectors.toList());
        if(isActive){
            accounts = accounts.stream().filter(account -> account.getAccountStatus().equals(AccountStatus.ACTIVE)).collect(Collectors.toList());
        }
        return accounts;
    }

    @Override
    public AccountDTO createAccount(Long id) {
        Account account = Account.builder().accountNumber(generateRandomCardNumber()).accountStatus(AccountStatus.INACTIVE).balance(100.0).customer(customerRepository.findById(id).orElseThrow(IllegalArgumentException::new)).build();
        return accountMapper.accountToAccountDTO(accountRepository.save(account));
    }

    @Override
    public AccountDTO changeStatus(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
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
        accountRepository.delete(accountRepository.findByAccountNumber(accountNumber));
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
