package org.example.servlets;

import org.example.dto.Product;
import org.example.exception.custom.AuthenticationException;
import org.example.exception.custom.OrderException;
import org.example.exception.custom.PaymentException;
import org.example.logic.LogicHandler;
import org.example.responseEntities.OrderResponse;
import org.example.responseEntities.PaymentResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ShopController {

    @Autowired
    LogicHandler logicHandler;

    @PostMapping("/addProduct")
    public void addProduct(@RequestParam(name = "auth_key") String authKey, @RequestBody String body) throws AuthenticationException {
        logicHandler.addProduct(authKey, body);
    }

    @GetMapping("/allProducts")
    public List<Product> getAllProducts() {
        return logicHandler.getAllProducts();
    }

    @PostMapping("/placeOrder")
    public OrderResponse placeOrder(@RequestBody String body) throws OrderException {
        return logicHandler.placeOrder(body);

    }

    @PostMapping("/doPayment")
    public PaymentResponse doPayment(@RequestBody String body) throws PaymentException {
        return logicHandler.doPayment(body);
    }
}
