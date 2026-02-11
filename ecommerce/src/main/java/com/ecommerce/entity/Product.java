package com.ecommerce.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    @Column(nullable = false)
    private Double price;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, length = 5000)
    private String description;

    @Column(nullable = false)
    private Integer stock = 0;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private boolean active = true;


    private String coverImage;
    private String image1;
    private String image2;
}
