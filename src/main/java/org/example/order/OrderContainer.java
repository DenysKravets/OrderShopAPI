package org.example.order;

import org.example.dto.Order;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Vector;

@Service
public class OrderContainer  {

    private final Vector<Order> orders = new Vector<>();

    synchronized public void addOrder(Order order) {
        orders.add(order);
    }

    synchronized public Iterator<Order> getIterator() {
        return orders.iterator();
    }
}
