package com.matrix.Transaction.exception;

public class TransactionNotFound extends ResourceNotFoundException{
    public TransactionNotFound(String message) {
        super("TransactionNotFound: "+message);
    }
}
