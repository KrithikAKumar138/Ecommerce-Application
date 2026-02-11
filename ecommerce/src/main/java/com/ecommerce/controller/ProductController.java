package com.ecommerce.controller;

import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private  ProductService productService;


    @GetMapping("/products")
    public String products(
            @RequestParam(required = false) String keyword,
            Model model) {

        List<Product> products;

        if (keyword != null && !keyword.trim().isEmpty()) {
            products = productService.search(keyword);
        } else {
            products = productService.getAllProducts();
        }

        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);

        return "product-list";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id, Model model) {

        Product product = productService.getById(id);

        model.addAttribute("product", product);

        return "product-detail";
    }
}
