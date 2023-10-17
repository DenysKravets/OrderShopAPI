package org.example.exception;

import org.example.exception.custom.AuthenticationException;
import org.example.exception.custom.OrderException;
import org.example.exception.custom.PaymentException;
import org.example.responseEntities.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(AuthenticationException.class)
     public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception e) {
         HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
         return new ResponseEntity<>(new ErrorResponse(httpStatus, e.getMessage()), httpStatus);
     }

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderException(Exception e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(httpStatus, e.getMessage()), httpStatus);
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<ErrorResponse> handlePaymentException(Exception e) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ErrorResponse(httpStatus, e.getMessage()), httpStatus);
    }
}
