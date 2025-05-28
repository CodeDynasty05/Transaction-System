package com.matrix.Transaction.controller;

import com.matrix.Transaction.model.dto.CustomerAddRequestDto;
import com.matrix.Transaction.model.dto.CustomerDto;
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
    public List<CustomerDto> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/{id}")
    private CustomerDto getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    private CustomerDto createCustomer(@RequestBody CustomerAddRequestDto customer) {
        return customerService.createCustomer(customer);
    }

    @PutMapping("/{id}")
    private CustomerDto updateCustomer(@PathVariable Long id, @RequestBody CustomerAddRequestDto customer) {
        return customerService.updateCustomer(id,customer);
    }

    @DeleteMapping("/{id}")
    private void deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
    }

}
