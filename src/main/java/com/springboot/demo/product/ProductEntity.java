package com.springboot.demo.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Quality is required")
    private String quality;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    public ProductEntity(long l, String string, double d) {
        this.id = l;
        this.name = string;
        this.price = d;

    }
    public ProductEntity() {
        //TODO Auto-generated constructor stub
    }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getQuality() { return quality; }
    public void setQuality(String quality) { this.quality = quality; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
}