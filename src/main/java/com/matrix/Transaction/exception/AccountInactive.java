package com.matrix.Transaction.exception;

public class AccountInactive extends Forbidden {
    public AccountInactive(String message) {
        super("AccountInactive" + message);
    }
}
