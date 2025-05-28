package com.matrix.Transaction.exception;

public class StatusNotSuitableException extends ForbiddenException {
    public StatusNotSuitableException(String message) {
        super("StatusNotSuitable: " + message);
    }
}
