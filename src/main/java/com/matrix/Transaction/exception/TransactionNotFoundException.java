package com.matrix.Transaction.exception;

public class TransactionNotFoundException extends ResourceNotFoundException{
    public TransactionNotFoundException(String message) {
        super("TransactionNotFound: "+message);
    }
}
