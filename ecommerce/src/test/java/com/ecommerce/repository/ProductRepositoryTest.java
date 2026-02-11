package com.ecommerce.repository;

import com.ecommerce.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findByNameContainingIgnoreCaseAndActiveTrue_shouldWork() {
        Product p = new Product();
        p.setName("iPhone");
        p.setPrice(1000.0);
        p.setDescription("Apple phone");
        p.setCategory("Mobile");
        p.setStock(10);
        p.setActive(true); // ✅ IMPORTANT

        productRepository.save(p);

        List<Product> result =
                productRepository.findByNameContainingIgnoreCaseAndActiveTrue("iphone");

        boolean found = result.stream()
                .anyMatch(prod -> prod.getName().equals("iPhone"));

        assertTrue(found, "Expected product 'iPhone' to be found in search results");
    }
}
