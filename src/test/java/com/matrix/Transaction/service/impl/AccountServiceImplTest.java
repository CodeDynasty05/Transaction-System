package com.matrix.Transaction.service.impl;

import com.matrix.Transaction.exception.AccountNotFoundException;
import com.matrix.Transaction.exception.CustomerNotFoundException;
import com.matrix.Transaction.mapper.AccountMapper;
import com.matrix.Transaction.model.dto.AccountDto;
import com.matrix.Transaction.model.entity.Account;
import com.matrix.Transaction.model.entity.AccountStatus;
import com.matrix.Transaction.model.entity.Customer;
import com.matrix.Transaction.repository.AccountRepository;
import com.matrix.Transaction.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private AccountDto accountDto;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);

        account = Account.builder()
                .accountNumber("1234567890123456")
                .balance(100.0)
                .accountStatus(AccountStatus.ACTIVE)
                .customer(customer)
                .build();

        accountDto = new AccountDto();
        accountDto.setAccountNumber("1234567890123456");
        accountDto.setBalance(100.0);
        accountDto.setAccountStatus(AccountStatus.ACTIVE);
    }

    @Test
    void testGetAccountsReturnsActiveAccounts() {
        when(accountRepository.findByCustomerId(1L)).thenReturn(List.of(account));
        when(accountMapper.accountToAccountDTO(account)).thenReturn(accountDto);

        List<AccountDto> result = accountService.getAccounts(1L, true);

        assertEquals(1, result.size());
        verify(accountRepository).findByCustomerId(1L);
    }

    @Test
    void testGetAccountsNoAccountsFoundThrowsException() {
        when(accountRepository.findByCustomerId(1L)).thenReturn(Collections.emptyList());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccounts(1L, true));
    }

    @Test
    void testCreateAccountSuccess() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.accountToAccountDTO(account)).thenReturn(accountDto);

        AccountDto result = accountService.createAccount(1L);

        assertNotNull(result);
        assertEquals(accountDto.getAccountNumber(), result.getAccountNumber());
    }

    @Test
    void testCreateAccountCustomerNotFoundThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> accountService.createAccount(1L));
    }

    @Test
    void testChangeStatusTogglesStatus() {
        account.setAccountStatus(AccountStatus.ACTIVE);
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.accountToAccountDTO(account)).thenReturn(accountDto);

        AccountDto result = accountService.changeStatus("123");

        assertEquals(AccountStatus.INACTIVE, account.getAccountStatus());
        assertEquals(accountDto, result);
    }

    @Test
    void testChangeStatusAccountNotFound() {
        when(accountRepository.findByAccountNumber("999")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.changeStatus("999"));
    }

    @Test
    void testIncreaseAccountBalanceSuccess() {
        account.setAccountStatus(AccountStatus.ACTIVE);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.accountToAccountDTO(account)).thenReturn(accountDto);

        AccountDto result = accountService.increaseAccountBalance(account, 50.0);

        assertEquals(150.0, account.getBalance());
        assertEquals(accountDto, result);
    }

    @Test
    void testDecreaseAccountBalanceSuccess() {
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setBalance(200.0);
        when(accountRepository.save(account)).thenReturn(account);
        when(accountMapper.accountToAccountDTO(account)).thenReturn(accountDto);

        AccountDto result = accountService.decreaseAccountBalance(account, 100.0);

        assertEquals(100.0, account.getBalance());
        assertEquals(accountDto, result);
    }

    @Test
    void testDeleteAccountSuccess() {
        when(accountRepository.findByAccountNumber("123")).thenReturn(Optional.of(account));

        accountService.deleteAccount("123");

        verify(accountRepository).delete(account);
    }

    @Test
    void testDeleteAccountNotFound() {
        when(accountRepository.findByAccountNumber("999")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount("999"));
    }
}