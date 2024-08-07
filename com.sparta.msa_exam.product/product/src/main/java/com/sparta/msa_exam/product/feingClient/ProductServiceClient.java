package com.sparta.msa_exam.product.feingClient;


import com.sparta.msa_exam.product.entity.ProductEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "product-service")
public interface ProductServiceClient {
    @PostMapping("/products")
    ResponseEntity<ProductEntity> createProduct(@RequestBody ProductEntity product);
}
