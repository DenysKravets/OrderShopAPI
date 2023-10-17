package org.example.logic;

import org.example.database.DBService;
import org.example.dto.Order;
import org.example.dto.Product;
import org.example.exception.custom.AuthenticationException;
import org.example.exception.custom.OrderException;
import org.example.exception.custom.PaymentException;
import org.example.order.OrderContainer;
import org.example.payment.PaymentHandler;
import org.example.responseEntities.OrderResponse;
import org.example.responseEntities.PaymentResponse;
import org.example.security.Credentials;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class LogicHandler {

    @Autowired
    DBService dbService;
    @Autowired
    OrderContainer orderContainer;
    @Autowired
    PaymentHandler paymentHandler;

    public void addProduct(String authKey, String body) throws AuthenticationException {
        // Check credentials
        if (!authKey.equals(Credentials.AUTH_KEY))
            throw new AuthenticationException("Incorrect authentication key.");

        Product product = new Product(body);
        dbService.save(product);
    }

    public List<Product> getAllProducts() {
        return dbService.getAllProducts();
    }

    public OrderResponse placeOrder(String body) throws OrderException {
        Order order = new Order(body);
        Product product = dbService.findProductById(order.getProductId());

        if (product == null)
            throw new OrderException("Specified product doesn't exist.");

        if (order.getQuantity() > product.getQuantity())
            throw new OrderException("Not enough product quantity.");

        int currentProductQuantity = product.getQuantity();
        product.setQuantity(currentProductQuantity - order.getQuantity());

        dbService.update(product);

        order.setId(UUID.randomUUID().toString());
        order.setPaymentId(UUID.randomUUID().toString());

        orderContainer.addOrder(order);

        return new OrderResponse(order);
    }

    public PaymentResponse doPayment(String body) throws PaymentException {

        JSONObject jsonObject = new JSONObject(body);
        String paymentId = jsonObject.getString("paymentId");
        String paymentInfo = jsonObject.getString("paymentInfo");

        boolean paymentSuccessful = false;

        Iterator<Order> orderIterator = orderContainer.getIterator();
        while (orderIterator.hasNext()) {
            Order order = orderIterator.next();
            if (order.getPaymentId().equals(paymentId)) {
                if (paymentHandler.paymentSuccessful(paymentInfo)) {
                    orderIterator.remove();
                    dbService.save(order);
                    paymentSuccessful = true;
                    break;
                }
                break;
            }
        }

        if (paymentSuccessful)
            return new PaymentResponse(paymentId, "Payment Successful.");

        throw new PaymentException("Payment credentials incorrect or expired.");
    }
}
