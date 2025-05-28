package com.matrix.Transaction.model.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CustomerAddRequestDto {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate dateOfBirth;
}
