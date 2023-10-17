package org.example.database;

import org.example.dao.OrderDao;
import org.example.dao.ProductDao;
import org.example.dto.Order;
import org.example.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DBService {

    @Autowired
    OrderDao orderDao;

    @Autowired
    ProductDao productDao;

    public void save(Order order) {
        orderDao.save(order);
    }

    public void save(Product product) {
        // Don't allow non unique names
        Product foundProduct = findProductByName(product.getName());
        if (foundProduct != null)
            return;
        // Generate id
        if (product.getId() == null)
            product.setId(UUID.randomUUID().toString());
        // Create
        productDao.save(product);
    }

    public void update(Product product) {
        // Don't update something that doesn't exist
        Product foundProduct = findProductByName(product.getName());
        if (foundProduct == null)
            return;
        // Update
        foundProduct.setQuantity(product.getQuantity());
        productDao.save(foundProduct);
    }

    public List<Product> getAllProducts() {
        List<Product> productList = new ArrayList<>();
        for (Product product : productDao.findAll())
            productList.add(product);
        return productList;
    }

    public Product findProductByName(String name) {
        return productDao.findByName(name);
    }

    public Product findProductById(String id) {
        return productDao.findById(id).orElse(null);
    }
}
