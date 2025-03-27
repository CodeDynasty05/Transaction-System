package com.matrix.Transaction.controller;

import com.matrix.Transaction.model.dto.CustomerAddRequestDTO;
import com.matrix.Transaction.model.dto.CustomerDTO;
import com.matrix.Transaction.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @GetMapping
    public List<CustomerDTO> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/{id}")
    private CustomerDTO getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    private CustomerDTO createCustomer(@RequestBody CustomerAddRequestDTO customer) {
        return customerService.createCustomer(customer);
    }

    @PutMapping("/{id}")
    private CustomerDTO updateCustomer(@PathVariable Long id, @RequestBody CustomerAddRequestDTO customer) {
        return customerService.updateCustomer(id,customer);
    }

    @DeleteMapping("/{id}")
    private void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

}
