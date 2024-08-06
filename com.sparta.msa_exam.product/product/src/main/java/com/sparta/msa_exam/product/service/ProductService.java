package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.ProductResponse;
import com.sparta.msa_exam.product.entity.ProductEntity;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
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
        List<ProductEntity> productEntities = productRepository.findAll();
        if (productEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<ProductResponse> productResponses = productEntities.stream()
                    .map(this::convertToProductResponse) // 변환 메서드 예시
                    .collect(Collectors.toList());
            return new ResponseEntity<>(productResponses, HttpStatus.OK);
        }
    }

    private ProductResponse convertToProductResponse(ProductEntity productEntity) {
        return new ProductResponse(productEntity.getProductId(), productEntity.getName(), productEntity.getSupplyPrice());
    }


}
