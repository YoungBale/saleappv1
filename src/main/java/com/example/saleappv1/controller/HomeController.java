package com.example.saleappv1.controller;

import com.example.saleappv1.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller handling homepage requests.
 */
@Controller
public class HomeController {

    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Maps GET "/" and returns the "index" view.
     *
     * @param model UI model to pass data to the template
     * @return the view name "index"
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("categories", productService.getAllCategories());
        return "index";
    }
}
