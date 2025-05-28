package com.matrix.Transaction.exception;

public class PhoneAlreadyExistsException extends ForbiddenException {
    public PhoneAlreadyExistsException(String message) {
        super("PhoneAlreadyExists: "+message);
    }
}
