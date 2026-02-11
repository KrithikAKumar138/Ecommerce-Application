package com.ecommerce.controller;

import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private  ProductService productService;

    @GetMapping("/dashboard")
    public String userDashboard(
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        List<Product> products;

        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.search(keyword);
        } else {
            // When user logs in first time → show all products
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);

        return "user-dashboard";
    }

    @GetMapping("/products")
    public String userProducts(
            @RequestParam(required = false) String category,
            Model model
    ) {
        List<Product> products;

        if (category != null && !category.trim().isEmpty()) {
            products = productService.getByCategory(category);
            model.addAttribute("selectedCategory", category);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        return "user-products";
    }
}
