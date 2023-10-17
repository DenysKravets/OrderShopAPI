package org.example.dao;

import org.example.dto.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductDao extends CrudRepository<Product, String> {
    Product findByName(String name);

}
