package com.ecommerce.service;

import com.ecommerce.entity.Product;
import com.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

    @Autowired
    private  ProductRepository productRepository;
    private final String UPLOAD_DIR = "uploads/";


    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrue();
    }


    public List<Product> search(String keyword) {
        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(keyword);
    }


    public List<Product> getByCategory(String category) {
        return productRepository.findByCategoryAndActiveTrue(category);
    }


    public Product getById(Long id) {
        return productRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new RuntimeException("Product not found or inactive"));
    }


    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        product.setActive(false);
        productRepository.save(product);
    }


    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void saveProductWithImages(Product product,
                                      MultipartFile coverImageFile,
                                      MultipartFile image1File,
                                      MultipartFile image2File) throws Exception {

        Files.createDirectories(Paths.get(UPLOAD_DIR));

        if (coverImageFile != null && !coverImageFile.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + coverImageFile.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.write(path, coverImageFile.getBytes());
            product.setCoverImage(fileName);
        }

        if (image1File != null && !image1File.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + image1File.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.write(path, image1File.getBytes());
            product.setImage1(fileName);
        }

        if (image2File != null && !image2File.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + image2File.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.write(path, image2File.getBytes());
            product.setImage2(fileName);
        }

        productRepository.save(product);
    }


    public void updateProduct(Long id, Product formProduct) {
        Product dbProduct = getById(id);

        dbProduct.setName(formProduct.getName());
        dbProduct.setPrice(formProduct.getPrice());
        dbProduct.setDescription(formProduct.getDescription());
        dbProduct.setStock(formProduct.getStock());
        dbProduct.setCategory(formProduct.getCategory());

        productRepository.save(dbProduct);
    }
    public void reduceStock(Product product, int qty) {
        if (product.getStock() < qty) {
            throw new RuntimeException("Not enough stock for " + product.getName());
        }
        product.setStock(product.getStock() - qty);
        productRepository.save(product);
    }
}
