package com.wecp.progressive.exception;

public class OutOfBalanceException extends RuntimeException {
    public OutOfBalanceException(String msg) {
        super(msg);
    }
}