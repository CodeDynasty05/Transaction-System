package com.matrix.Transaction.service.impl;

import com.matrix.Transaction.exception.CustomerNotFoundException;
import com.matrix.Transaction.exception.EmailAlreadyExistsException;
import com.matrix.Transaction.exception.PhoneAlreadyExistsException;
import com.matrix.Transaction.mapper.CustomerMapper;
import com.matrix.Transaction.model.dto.CustomerAddRequestDto;
import com.matrix.Transaction.model.dto.CustomerDto;
import com.matrix.Transaction.model.entity.Customer;
import com.matrix.Transaction.repository.CustomerRepository;
import com.matrix.Transaction.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Mock
    private AccountService accountService;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customer;
    private CustomerDto customerDto;
    private CustomerAddRequestDto customerAddRequestDto;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john@example.com");
        customer.setPhone("123456789");

        customerDto = new CustomerDto();
        customerDto.setId(1L);
        customerDto.setFirstName("John");
        customerDto.setLastName("Doe");
        customerDto.setEmail("john@example.com");
        customerDto.setPhone("123456789");

        customerAddRequestDto = new CustomerAddRequestDto();
        customerAddRequestDto.setFirstName("John");
        customerAddRequestDto.setLastName("Doe");
        customerAddRequestDto.setEmail("john@example.com");
        customerAddRequestDto.setPhone("123456789");
    }

    @Test
    void testGetCustomers() {
        when(customerRepository.findAll()).thenReturn(List.of(customer));
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDto);

        List<CustomerDto> customers = customerService.getCustomers();
        assertEquals(1, customers.size());
        assertEquals("John", customers.get(0).getFirstName());
    }

    @Test
    void testGetCustomerSuccess() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDto);

        CustomerDto result = customerService.getCustomer(1L);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testGetCustomerThrowsException() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> customerService.getCustomer(1L));
    }

    @Test
    void testCreateCustomerSuccess() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);
        when(customerMapper.customerAddRequestDTOToCustomer(customerAddRequestDto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDto);

        CustomerDto result = customerService.createCustomer(customerAddRequestDto);
        assertEquals("John", result.getFirstName());
        verify(accountService).createAccount(customer.getId());
    }

    @Test
    void testCreateCustomerEmailExists() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(true);
        assertThrows(EmailAlreadyExistsException.class, () -> customerService.createCustomer(customerAddRequestDto));
    }

    @Test
    void testCreateCustomerPhoneExists() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByPhone(anyString())).thenReturn(true);
        assertThrows(PhoneAlreadyExistsException.class, () -> customerService.createCustomer(customerAddRequestDto));
    }

    @Test
    void testUpdateCustomerSuccess() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.updateCustomer(customerAddRequestDto, customer)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.customerToCustomerDTO(customer)).thenReturn(customerDto);

        CustomerDto result = customerService.updateCustomer(1L, customerAddRequestDto);
        assertEquals("John", result.getFirstName());
    }

    @Test
    void testUpdateCustomerCustomerNotFound() {
        when(customerRepository.existsByEmail(anyString())).thenReturn(false);
        when(customerRepository.existsByPhone(anyString())).thenReturn(false);
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(1L, customerAddRequestDto));
    }

    @Test
    void testDeleteCustomerSuccess() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        customerService.deleteCustomer(1L);
        verify(customerRepository).deleteById(1L);
    }

    @Test
    void testDeleteCustomerNotFound() {
        when(customerRepository.existsById(1L)).thenReturn(false);
        assertThrows(CustomerNotFoundException.class, () -> customerService.deleteCustomer(1L));
    }
}