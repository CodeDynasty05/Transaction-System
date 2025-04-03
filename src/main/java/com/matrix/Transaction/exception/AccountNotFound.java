package com.matrix.Transaction.exception;

public class AccountNotFound extends ResourceNotFoundException{
    public AccountNotFound(String message) {
        super("AccountNotFound: " + message);
    }
}
