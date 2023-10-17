package org.example.dao;

import org.example.dto.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderDao extends CrudRepository<Order, String> {

}
