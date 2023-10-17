package org.example.dto;

import org.json.JSONObject;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(unique = true, name = "identification_key")
    private String id;
    @Column(name = "productId")
    private String productId;
    @Column(name = "quantity")
    private int quantity;
    @Transient
    private final Date startDate = new Date();
    @Transient
    private String paymentId = UUID.randomUUID().toString();

    public Order() {}

    public Order(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Order(String id, String productId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Order(String jsonBody) {
        JSONObject jsonObject = new JSONObject(jsonBody);
        this.productId = jsonObject.getString("productId");
        this.quantity = jsonObject.getInt("quantity");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getStartDate() {
        return startDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
