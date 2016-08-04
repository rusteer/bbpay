package com.bbpay.server.service;
public class PayException extends RuntimeException {
    private static final long serialVersionUID = 5022546956574536309L;
    public PayException(String msg) {
        super(msg);
    }
}
