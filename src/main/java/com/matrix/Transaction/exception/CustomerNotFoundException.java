package com.matrix.Transaction.exception;

public class CustomerNotFoundException extends ResourceNotFoundException {
    public CustomerNotFoundException(String message) {
        super("CustomerNotFound: " + message);
    }
}
