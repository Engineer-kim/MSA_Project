package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.dto.OrderResponse;
import com.sparta.msa_exam.order.entity.OrderEntity;
import com.sparta.msa_exam.order.entity.OrderMappingProductEntity;
import com.sparta.msa_exam.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    @Transactional
    public ResponseEntity<OrderEntity> save(OrderEntity order) {
        if (order != null) {
            if (order.getProductList() != null) {
                for (OrderMappingProductEntity product : order.getProductList()) {
                    product.setOrderEntity(order);
                }
            }
            OrderEntity savedOrder = orderRepository.save(order);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @Transactional
    public ResponseEntity<String> updateOrder(Long orderId, Long productId) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            OrderEntity existingOrder = optionalOrder.get();
            boolean productExists = existingOrder.getProductIds().contains(productId);
            if (productExists) {
                return new ResponseEntity<>("해당 상품이 이미 주문에 존재합니다.", HttpStatus.BAD_REQUEST);
            }
            OrderMappingProductEntity product = new OrderMappingProductEntity();
            product.setProductId(productId);
            product.setOrderEntity(existingOrder);
            existingOrder.addProduct(product);
            orderRepository.save(existingOrder);

            return new ResponseEntity<>("주문에 상품이 추가 되었습니다", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("주문 정보 업데이트에 실패했습니다", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 단건 조회
     * */
    @Cacheable(cacheNames = "orders", key = "#orderId")
    public OrderResponse getOneOrder(Long orderId) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.map(order -> {
                    List<Long> productIds = order.getProductIds();
                    return new OrderResponse(order.getOrderId(), productIds.stream()
                            .map(Long::intValue)
                            .collect(Collectors.toList()));
                })
                .orElse(null);
    }

}
