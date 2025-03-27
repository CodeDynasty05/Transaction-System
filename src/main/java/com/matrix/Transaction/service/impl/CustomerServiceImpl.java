package com.matrix.Transaction.service.impl;

import com.matrix.Transaction.mapper.CustomerMapper;
import com.matrix.Transaction.model.dto.CustomerAddRequestDTO;
import com.matrix.Transaction.model.dto.CustomerDTO;
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
    public List<CustomerDTO> getCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::customerToCustomerDTO).collect(Collectors.toList());
    }

    @Override
    public CustomerDTO getCustomer(Long id) {
        return customerMapper.customerToCustomerDTO(customerRepository.findById(id).orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public CustomerDTO createCustomer(CustomerAddRequestDTO customer) {
        Customer customerEntity = customerRepository.save(customerMapper.customerAddRequestDTOToCustomer(customer));
        accountService.createAccount(customerEntity.getId());
        return customerMapper.customerToCustomerDTO(customerEntity);
    }

    @Override
    public CustomerDTO updateCustomer(Long id, CustomerAddRequestDTO customer) {
        Customer customerEntity = customerRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        Customer newCustomer = customerMapper.updateCustomer(customer, customerEntity);
        return customerMapper.customerToCustomerDTO(customerRepository.save(newCustomer));
    }

    @Override
    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }
}
