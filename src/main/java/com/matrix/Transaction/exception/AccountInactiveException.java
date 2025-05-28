package com.matrix.Transaction.exception;

public class AccountInactiveException extends ForbiddenException {
    public AccountInactiveException(String message) {
        super("AccountInactive" + message);
    }
}
