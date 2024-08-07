package com.sparta.msa_exam.product.feignClient;


import com.sparta.msa_exam.product.dto.ProductResponse;
import com.sparta.msa_exam.product.entity.ProductEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "product-service" , url="http://localhost:19092")
public interface ProductServiceClient {
    @PostMapping("/products")
    ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product);
    @GetMapping("/products")
    ResponseEntity<List<ProductResponse>> getAllOrders();
}
