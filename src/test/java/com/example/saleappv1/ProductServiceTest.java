package com.example.saleappv1;

import com.example.saleappv1.model.Category;
import com.example.saleappv1.model.Product;
import com.example.saleappv1.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("Should load categories and products from JSON files successfully")
    void testDataLoaded() {
        List<Category> categories = productService.getAllCategories();
        assertNotNull(categories);
        assertEquals(2, categories.size());

        List<Product> products = productService.getAllProducts();
        assertNotNull(products);
        assertEquals(8, products.size());
    }

    @Test
    @DisplayName("Should find product by ID correctly")
    void testGetProductById() {
        Product p = productService.getProductById(1);
        assertNotNull(p);
        assertEquals("iPhone 17 Pro Max Burgundy 256GB", p.getName());
        assertEquals(1, p.getCategoryId());

        Product nonExistent = productService.getProductById(999);
        assertNull(nonExistent);
    }

    @Test
    @DisplayName("Should filter products by category ID")
    void testFilterByCategory() {
        List<Product> phones = productService.getFilteredProducts(1, null, null, null);
        assertNotNull(phones);
        assertEquals(4, phones.size());
        assertTrue(phones.stream().allMatch(p -> p.getCategoryId() == 1));

        List<Product> tablets = productService.getFilteredProducts(2, null, null, null);
        assertNotNull(tablets);
        assertEquals(4, tablets.size());
        assertTrue(tablets.stream().allMatch(p -> p.getCategoryId() == 2));
    }

    @Test
    @DisplayName("Should filter products by keyword")
    void testFilterByKeyword() {
        List<Product> iphones = productService.getFilteredProducts(null, "iPhone", null, null);
        assertNotNull(iphones);
        assertFalse(iphones.isEmpty());
        assertTrue(iphones.stream().anyMatch(p -> p.getName().contains("iPhone")));

        List<Product> snapdragon = productService.getFilteredProducts(null, "Snapdragon", null, null);
        assertNotNull(snapdragon);
        assertFalse(snapdragon.isEmpty());
    }

    @Test
    @DisplayName("Should filter products by price range")
    void testFilterByPriceRange() {
        List<Product> affordable = productService.getFilteredProducts(null, null, 10000000.0, 20000000.0);
        assertNotNull(affordable);
        assertTrue(affordable.stream().allMatch(p -> p.getPrice() >= 10000000.0 && p.getPrice() <= 20000000.0));
    }
}
