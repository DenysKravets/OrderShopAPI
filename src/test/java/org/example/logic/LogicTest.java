package org.example.logic;

import org.example.database.DBService;
import org.example.dto.Product;
import org.example.exception.custom.AuthenticationException;
import org.example.exception.custom.OrderException;
import org.example.exception.custom.PaymentException;
import org.example.security.Credentials;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY, connection = EmbeddedDatabaseConnection.H2)
public class LogicTest {

    @Autowired
    private LogicHandler logicHandler;

    @Autowired
    private DBService dbService;

    @Test
    public void testAddProductWrongKey() {
        assertThatThrownBy(() -> {
            logicHandler.addProduct("wrong_key", "not_important");
        }).isInstanceOf(AuthenticationException.class);
    }

    @DirtiesContext
    @Test
    public void testAddProduct() throws AuthenticationException {
        String body = "{\n" +
                "\t\"name\": \"keyboard\",\n" +
                "\t\"quantity\": 10,\n" +
                "\t\"price\": 1.4\n" +
                "}";
        logicHandler.addProduct(Credentials.AUTH_KEY, body);

        Product foundProduct = dbService.findProductByName("keyboard");

        assertThat(foundProduct).isNotNull();
    }

    @DirtiesContext
    @Test
    public void testGetAllProducts() throws AuthenticationException {
        String body = "{\n" +
                "\t\"name\": \"keyboard\",\n" +
                "\t\"quantity\": 10,\n" +
                "\t\"price\": 1.4\n" +
                "}";
        logicHandler.addProduct(Credentials.AUTH_KEY, body);

        List<Product> productList = logicHandler.getAllProducts();

        assertThat(productList.size()).isEqualTo(1);
        assertThat(productList.get(0).getQuantity()).isEqualTo(10);
    }

    @DirtiesContext
    @Test
    public void testPlaceOrderProductNotExist() {
        assertThatThrownBy(() -> {
            String body = "{\n" +
                    "\t\"productId\": \"incorrect_product_id\",\n" +
                    "\t\"quantity\": 1\n" +
                    "}";
            logicHandler.placeOrder(body);
        }).isInstanceOf(OrderException.class)
                .hasMessage("Specified product doesn't exist.");
    }

    @DirtiesContext
    @Test
    public void testPlaceOrderNotEnoughQuantity() {
        assertThatThrownBy(() -> {

            String body = "{\n" +
                    "\t\"name\": \"keyboard\",\n" +
                    "\t\"quantity\": 10,\n" +
                    "\t\"price\": 1.4\n" +
                    "}";
            logicHandler.addProduct(Credentials.AUTH_KEY, body);
            List<Product> productList = logicHandler.getAllProducts();
            body = "{\n" +
                    "\t\"productId\": \"" + productList.get(0).getId() + "\",\n" +
                    "\t\"quantity\": 11\n" +
                    "}";
            logicHandler.placeOrder(body);
        }).isInstanceOf(OrderException.class)
                .hasMessage("Not enough product quantity.");
    }

    @DirtiesContext
    @Test
    public void testDoPaymentIncorrectOrExpired() {
        assertThatThrownBy(() -> {
            String body = "{\n" +
                    "\t\"paymentId\": \"not_correct_payment_id\",\n" +
                    "\t\"paymentInfo\": \"1\"\n" +
                    "}";
            logicHandler.doPayment(body);
        }).isInstanceOf(PaymentException.class)
                .hasMessage("Payment credentials incorrect or expired.");
    }

}
