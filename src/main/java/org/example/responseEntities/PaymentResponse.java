package org.example.responseEntities;

public class PaymentResponse {

    private String paymentId;
    private String message;

    public PaymentResponse(String paymentId, String message) {
        this.paymentId = paymentId;
        this.message = message;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
