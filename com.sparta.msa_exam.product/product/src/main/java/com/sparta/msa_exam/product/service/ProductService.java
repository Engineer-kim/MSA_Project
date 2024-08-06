package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.ProductResponse;
import com.sparta.msa_exam.product.entity.ProductEntity;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ResponseEntity<?> save(ProductEntity product) {
        if (product != null) {
            ProductEntity savedProduct = productRepository.save(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("상품을 추가 할수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }
    public ResponseEntity<List<ProductResponse>> getAllOrders() {
        Optional<List<ProductResponse>> optionalOrders = productRepository.getAllOrders();
        if (optionalOrders.isEmpty() || optionalOrders.get().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>(optionalOrders.get(), HttpStatus.OK);
        }
    }


}
