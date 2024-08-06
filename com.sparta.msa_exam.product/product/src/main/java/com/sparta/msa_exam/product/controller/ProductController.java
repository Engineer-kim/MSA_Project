package com.sparta.msa_exam.product.controller;


import com.sparta.msa_exam.product.service.ProductService;
import com.sparta.msa_exam.product.dto.ProductResponse;
import com.sparta.msa_exam.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/products")
    public ResponseEntity<?> createProduct(@RequestBody ProductEntity product) {
        return productService.save(product);

    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductResponse>> getAllOrders() {
        return productService.getAllOrders();
    }
}
