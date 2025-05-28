package com.matrix.Transaction.exception;

public class AccountNotFoundException extends ResourceNotFoundException{
    public AccountNotFoundException(String message) {
        super("AccountNotFound: " + message);
    }
}
