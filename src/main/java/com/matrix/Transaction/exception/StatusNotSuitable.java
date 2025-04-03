package com.matrix.Transaction.exception;

public class StatusNotSuitable extends Forbidden {
    public StatusNotSuitable(String message) {
        super("StatusNotSuitable: " + message);
    }
}
