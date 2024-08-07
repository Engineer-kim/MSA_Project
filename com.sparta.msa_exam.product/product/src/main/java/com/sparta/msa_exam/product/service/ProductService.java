package com.sparta.msa_exam.product.service;

import com.sparta.msa_exam.product.dto.ProductResponse;
import com.sparta.msa_exam.product.entity.ProductEntity;
import com.sparta.msa_exam.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    @CacheEvict(cacheNames = "productSaveCache", allEntries = true)
    public ResponseEntity<String> save(ProductEntity product) {
        if (product != null) {
             productRepository.save(product);
            return new ResponseEntity<>("상품 추가 성공", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("상품을 추가할 수 없습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @Cacheable(cacheNames = "productListCache")
    public ResponseEntity<List<ProductResponse>> getAllOrders() {
        List<ProductEntity> productEntities = productRepository.findAll();
        if (productEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            List<ProductResponse> productResponses = productEntities.stream()
                    .map(this::convertToProductResponse)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(productResponses, HttpStatus.OK);
        }
    }
    private ProductResponse convertToProductResponse(ProductEntity productEntity) {
        return new ProductResponse(productEntity.getProductId(), productEntity.getName(), productEntity.getSupplyPrice());
    }

    public ResponseEntity<List<Long>> fetchAllProductId() {
        List<ProductEntity> productEntities = productRepository.findAll();
        if (productEntities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<ProductResponse> productResponses = productEntities.stream()
                .map(this::convertToProductResponse)
                .toList();
        List<Long> productIds = productResponses.stream()
                .map(ProductResponse::getProductId)
                .collect(Collectors.toList());

        return new ResponseEntity<>(productIds, HttpStatus.OK);
    }
}
