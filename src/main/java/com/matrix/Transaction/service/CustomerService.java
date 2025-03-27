package com.matrix.Transaction.service;

import com.matrix.Transaction.model.dto.CustomerAddRequestDTO;
import com.matrix.Transaction.model.dto.CustomerDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public interface CustomerService {

    List<CustomerDTO> getCustomers();

    CustomerDTO getCustomer(Long id);

    CustomerDTO createCustomer(CustomerAddRequestDTO customer);

    CustomerDTO updateCustomer(Long id, CustomerAddRequestDTO customer);

    void deleteCustomer(Long id);
}
