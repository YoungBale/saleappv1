package com.example.saleappv1.controller;

import com.example.saleappv1.model.Category;
import com.example.saleappv1.model.Product;
import com.example.saleappv1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller handling product listings, search/filtering, and detailed product view.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Maps GET "/products" supporting optional filtering/search parameters.
     *
     * @param categoryId optional category filter ID
     * @param keyword    optional keyword search
     * @param fromPrice  optional lower bound for price
     * @param toPrice    optional upper bound for price
     * @param model      Spring MVC Model
     * @return "products" view name
     */
    @GetMapping
    public String getProducts(
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Double fromPrice,
            @RequestParam(required = false) Double toPrice,
            Model model) {

        List<Product> filteredProducts = productService.getFilteredProducts(categoryId, keyword, fromPrice, toPrice);
        List<Category> categories = productService.getAllCategories();

        model.addAttribute("products", filteredProducts);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("fromPrice", fromPrice);
        model.addAttribute("toPrice", toPrice);

        // Find selected category name if applicable
        if (categoryId != null) {
            Category selectedCategory = productService.getCategoryById(categoryId);
            if (selectedCategory != null) {
                model.addAttribute("selectedCategoryName", selectedCategory.getName());
            }
        }

        return "products";
    }

    /**
     * Maps GET "/products/{productId}" to display product details.
     *
     * @param productId product ID from URL path
     * @param model     Spring MVC Model
     * @return "product-detail" view name
     */
    @GetMapping("/{productId}")
    public String getProductDetail(@PathVariable("productId") int productId, Model model) {
        Product product = productService.getProductById(productId);

        if (product == null) {
            model.addAttribute("errorMessage", "Sản phẩm không tồn tại hoặc đã ngừng kinh doanh.");
            return "product-detail";
        }

        Category category = productService.getCategoryById(product.getCategoryId());

        model.addAttribute("product", product);
        model.addAttribute("category", category);

        return "product-detail";
    }
}
