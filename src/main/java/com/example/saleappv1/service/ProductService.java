package com.example.saleappv1.service;

import com.example.saleappv1.model.Category;
import com.example.saleappv1.model.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to manage products and categories loaded from JSON data files using Jackson ObjectMapper.
 */
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private static final String PRODUCTS_FILE_PATH = "src/main/resources/data/products.json";
    private static final String CATEGORIES_FILE_PATH = "src/main/resources/data/categories.json";

    private final ObjectMapper objectMapper;
    private List<Product> products = new ArrayList<>();
    private List<Category> categories = new ArrayList<>();

    public ProductService() {
        this.objectMapper = new ObjectMapper();
    }

    public ProductService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper != null ? objectMapper : new ObjectMapper();
    }

    /**
     * Initializes product and category lists from JSON files upon service initialization.
     */
    @PostConstruct
    public void init() {
        loadCategories();
        loadProducts();
    }

    /**
     * Loads category data from JSON file.
     */
    public synchronized void loadCategories() {
        try (InputStream is = getResourceInputStream("/data/categories.json", CATEGORIES_FILE_PATH)) {
            categories = objectMapper.readValue(is, new TypeReference<List<Category>>() {});
            logger.info("Successfully loaded {} categories from JSON.", categories.size());
        } catch (IOException e) {
            logger.error("Failed to load categories from JSON: {}", e.getMessage(), e);
            categories = new ArrayList<>();
        }
    }

    /**
     * Loads product data from JSON file.
     */
    public synchronized void loadProducts() {
        try (InputStream is = getResourceInputStream("/data/products.json", PRODUCTS_FILE_PATH)) {
            products = objectMapper.readValue(is, new TypeReference<List<Product>>() {});
            logger.info("Successfully loaded {} products from JSON.", products.size());
        } catch (IOException e) {
            logger.error("Failed to load products from JSON: {}", e.getMessage(), e);
            products = new ArrayList<>();
        }
    }

    /**
     * Returns all available products.
     *
     * @return list of all products
     */
    public List<Product> getAllProducts() {
        return Collections.unmodifiableList(products);
    }

    /**
     * Finds a product by its unique identifier.
     *
     * @param id the product ID
     * @return the matching Product, or null if not found
     */
    public Product getProductById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Filters products by category, search keyword, and price range.
     *
     * @param categoryId optional category filter
     * @param keyword    optional search keyword matching name or description
     * @param fromPrice  optional minimum price
     * @param toPrice    optional maximum price
     * @return filtered list of products
     */
    public List<Product> getFilteredProducts(Integer categoryId, String keyword, Double fromPrice, Double toPrice) {
        return products.stream()
                .filter(p -> {
                    // Filter by category
                    if (categoryId != null && categoryId > 0) {
                        if (p.getCategoryId() != categoryId) {
                            return false;
                        }
                    }
                    // Filter by search keyword in name or description
                    if (keyword != null && !keyword.trim().isEmpty()) {
                        String kw = keyword.trim().toLowerCase();
                        boolean nameMatch = p.getName() != null && p.getName().toLowerCase().contains(kw);
                        boolean descMatch = p.getDescription() != null && p.getDescription().toLowerCase().contains(kw);
                        if (!nameMatch && !descMatch) {
                            return false;
                        }
                    }
                    // Filter by minimum price
                    if (fromPrice != null) {
                        if (p.getPrice() < fromPrice) {
                            return false;
                        }
                    }
                    // Filter by maximum price
                    if (toPrice != null) {
                        if (p.getPrice() > toPrice) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns all available categories.
     *
     * @return list of categories
     */
    public List<Category> getAllCategories() {
        return Collections.unmodifiableList(categories);
    }

    /**
     * Finds a category by its ID.
     *
     * @param id the category ID
     * @return the matching Category, or null if not found
     */
    public Category getCategoryById(int id) {
        return categories.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Helper to retrieve InputStream from either the file system or classpath.
     */
    private InputStream getResourceInputStream(String classpathLocation, String fileLocation) throws IOException {
        // Priority 1: Check project source file path if available
        File file = new File(fileLocation);
        if (file.exists() && file.isFile()) {
            return new FileInputStream(file);
        }

        // Priority 2: Check standard classpath
        InputStream is = getClass().getResourceAsStream(classpathLocation);
        if (is != null) {
            return is;
        }

        // Priority 3: ClassPathResource loader
        String cleanClasspath = classpathLocation.startsWith("/") ? classpathLocation.substring(1) : classpathLocation;
        ClassPathResource cpr = new ClassPathResource(cleanClasspath);
        if (cpr.exists()) {
            return cpr.getInputStream();
        }

        throw new FileNotFoundException("Unable to find resource at: " + fileLocation + " or classpath:" + classpathLocation);
    }
}
