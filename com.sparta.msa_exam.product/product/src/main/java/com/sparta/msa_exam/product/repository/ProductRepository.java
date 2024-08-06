package com.sparta.msa_exam.product.repository;

import com.sparta.msa_exam.product.dto.ProductResponse;
import com.sparta.msa_exam.product.entity.ProductEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Long> {
    Optional<List<ProductResponse>> getAllOrders();
}
