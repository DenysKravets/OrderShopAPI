package org.example.database;

import org.example.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
public class DataBaseTest {

    @Autowired
    DBService dbService;

    @DirtiesContext
    @Test
    public void testSaveProduct() {
        dbService.save(new Product("Socks", 2, 69.99));
        Product product = dbService.findProductByName("Socks");
        assertThat(product.getQuantity()).isEqualTo(2);
    }

    @DirtiesContext
    @Test
    public void testSaveProductDuplicateName() {
        dbService.save(new Product("Socks", 2, 69.99));
        dbService.save(new Product("Socks", 4, 69.99));
        Product product = dbService.findProductByName("Socks");
        assertThat(product.getQuantity()).isEqualTo(2);
    }

    @DirtiesContext
    @Test
    public void testUpdateProduct() {
        dbService.save(new Product("Socks", 2, 69.99));
        dbService.update(new Product("Socks", 4, 69.99));
        Product product = dbService.findProductByName("Socks");
        assertThat(product.getQuantity()).isEqualTo(4);
    }

}
