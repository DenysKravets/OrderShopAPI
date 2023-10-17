package org.example.exception.custom;

public class AuthenticationException extends Exception {
    public AuthenticationException(String message) {
        super(message);
    }
}
