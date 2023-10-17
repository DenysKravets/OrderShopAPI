package org.example.responseEntities;

import org.example.dto.Order;

public class OrderResponse {

    private String paymentId;

    public OrderResponse(Order order) {
        this.paymentId = order.getPaymentId();
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
