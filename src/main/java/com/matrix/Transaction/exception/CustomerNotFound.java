package com.matrix.Transaction.exception;

public class CustomerNotFound extends ResourceNotFoundException {
    public CustomerNotFound(String message) {
        super("CustomerNotFound: " + message);
    }
}
