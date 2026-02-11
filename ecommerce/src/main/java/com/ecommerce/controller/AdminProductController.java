package com.ecommerce.controller;

import com.ecommerce.entity.Product;
import com.ecommerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private  ProductService productService;


    @GetMapping
    public String list(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "admin-product-list";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        return "admin-product-form";
    }

    @PostMapping("/add")
    public String save(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            @RequestParam("coverImageFile") MultipartFile coverImageFile,
            @RequestParam("image1File") MultipartFile image1File,
            @RequestParam("image2File") MultipartFile image2File
    ) throws Exception {

        if (result.hasErrors()) {
            return "admin-product-form";
        }

        productService.saveProductWithImages(product, coverImageFile, image1File, image2File);

        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        model.addAttribute("product", product);
        return "admin-product-form";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute("product") Product formProduct
    ) {
        productService.updateProduct(id, formProduct);
        return "redirect:/admin/products";
    }

    @GetMapping("/view/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productService.getById(id);
        model.addAttribute("product", product);
        return "admin-product-detail";
    }
}
