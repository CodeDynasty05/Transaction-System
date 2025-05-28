package com.matrix.Transaction.service;

import com.matrix.Transaction.model.dto.CustomerAddRequestDto;
import com.matrix.Transaction.model.dto.CustomerDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {

    List<CustomerDto> getCustomers();

    CustomerDto getCustomer(Long id);

    CustomerDto createCustomer(CustomerAddRequestDto customer);

    CustomerDto updateCustomer(Long id, CustomerAddRequestDto customer);

    void deleteCustomer(Long id);
}
