package org.example.exception.custom;

public class PaymentException extends Exception {
    public PaymentException(String message) {
        super(message);
    }
}
