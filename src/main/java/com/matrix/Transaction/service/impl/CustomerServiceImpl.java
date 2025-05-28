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
import com.matrix.Transaction.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AccountService accountService;

    @Override
    public List<CustomerDto> getCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDto getCustomer(Long id) {
        return customerMapper.customerToCustomerDTO(customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException(("Customer with id " +id+" not found"))));
    }

    @Override
    public CustomerDto createCustomer(CustomerAddRequestDto customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        } else if (customerRepository.existsByPhone(customer.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone already exists");
        }
        Customer customerEntity = customerRepository.save(customerMapper.customerAddRequestDTOToCustomer(customer));
        accountService.createAccount(customerEntity.getId());
        return customerMapper.customerToCustomerDTO(customerEntity);
    }

    @Override
    public CustomerDto updateCustomer(Long id, CustomerAddRequestDto customer) {
        if (customerRepository.existsByEmail(customer.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        } else if (customerRepository.existsByPhone(customer.getPhone())) {
            throw new PhoneAlreadyExistsException("Phone already exists");
        }
        Customer customerEntity = customerRepository.findById(id).orElseThrow(()->new CustomerNotFoundException("Customer with id " +id+" not found"));
        Customer newCustomer = customerMapper.updateCustomer(customer, customerEntity);
        return customerMapper.customerToCustomerDTO(customerRepository.save(newCustomer));
    }

    @Override
    public void deleteCustomer(Long id) {
        if (customerRepository.existsById(id)) {
            customerRepository.deleteById(id);
        } else {
            throw new CustomerNotFoundException("Customer with id " +id + " not found");
        }
    }
}
