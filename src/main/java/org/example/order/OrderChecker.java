package org.example.order;

import org.example.database.DBService;
import org.example.dto.Order;
import org.example.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class OrderChecker {

    @Autowired
    OrderContainer orderContainer;

    @Autowired
    DBService dbService;

    private void checkOrders() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
        System.out.println(sdf.format(new Date()) + "  Checking orders ...");
        Iterator<Order> orderIterator = orderContainer.getIterator();
        while (orderIterator.hasNext()) {
            Order order = orderIterator.next();
            if (orderExpired(order)) {
                orderIterator.remove();
                restoreOrderedProduct(order);
            }
        }
    }

    private boolean orderExpired(Order order) {
        Date thisInstant = new Date();
        long seconds = (thisInstant.getTime() - order.getStartDate().getTime()) / 1000;
        long maxSeconds = 60 * 10; // 10 Minutes
        return seconds > maxSeconds;
    }

    private void restoreOrderedProduct(Order order) {
        Product product = dbService.findProductById(order.getProductId());
        int currentProductQuantity = product.getQuantity();
        product.setQuantity(currentProductQuantity + order.getQuantity());
        dbService.update(product);
    }

    @PostConstruct
    public void runChecker() {
        // Gets current moment in time
        Calendar firstRunDateTime = Calendar.getInstance();
        // Run every 30 seconds
        Timer timer = new Timer();
        long duration = TimeUnit.MILLISECONDS.convert(30, TimeUnit.SECONDS);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkOrders();
            }
        }, firstRunDateTime.getTime(), duration);
    }
}
