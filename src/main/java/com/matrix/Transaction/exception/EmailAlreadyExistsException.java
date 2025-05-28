package com.matrix.Transaction.exception;

public class EmailAlreadyExistsException extends ForbiddenException {
    public EmailAlreadyExistsException(String message) {
        super("EmailAlreadyExists: "+message);
    }
}
