package com.sparta.msa_exam.order.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;

    private String name;


    @OneToMany(mappedBy = "orderEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMappingProductEntity> productIds = new ArrayList<>();

    public void addProduct(OrderMappingProductEntity product) {
        product.setOrderEntity(this);
        productIds.add(product);
    }

    public List<Long> getProductIds() {
        return productIds.stream()
                .map(OrderMappingProductEntity::getProductId)
                .collect(Collectors.toList());
    }

    public List<OrderMappingProductEntity> getProductList() {
        return productIds;
    }
}