package com.example.saleappv1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class ProductControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    @DisplayName("GET / should return index view with categories")
    void testIndexEndpoint() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(content().string(containsString("BÁN HÀNG TRỰC TUYẾN")));
    }

    @Test
    @DisplayName("GET /products should return products view with all products")
    void testProductsEndpoint() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("products", hasSize(8)))
                .andExpect(content().string(containsString("iPhone 17 Pro Max Burgundy 256GB")));
    }

    @Test
    @DisplayName("GET /products with categoryId=1 should return only phones")
    void testProductsWithCategoryFilter() throws Exception {
        mockMvc.perform(get("/products").param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attribute("products", hasSize(4)))
                .andExpect(model().attribute("selectedCategoryName", "Điện thoại di động"));
    }

    @Test
    @DisplayName("GET /products/{productId} should return product-detail view for existing product")
    void testProductDetailEndpoint() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-detail"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attributeExists("category"))
                .andExpect(content().string(containsString("iPhone 17 Pro Max Burgundy 256GB")))
                .andExpect(content().string(containsString("Điện thoại di động")));
    }

    @Test
    @DisplayName("GET /products/{productId} with invalid id should return product-detail with error message")
    void testProductDetailNotFound() throws Exception {
        mockMvc.perform(get("/products/9999"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-detail"))
                .andExpect(model().attributeDoesNotExist("product"))
                .andExpect(content().string(containsString("Sản phẩm không tồn tại")));
    }
}
