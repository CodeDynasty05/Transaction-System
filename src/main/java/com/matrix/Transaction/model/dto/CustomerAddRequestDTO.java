package com.matrix.Transaction.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerAddRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
}
