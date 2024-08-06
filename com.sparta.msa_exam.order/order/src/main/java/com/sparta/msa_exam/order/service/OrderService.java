package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.dto.OrderResponse;
import com.sparta.msa_exam.order.entity.OrderEntity;
import com.sparta.msa_exam.order.entity.OrderMappingProductEntity;
import com.sparta.msa_exam.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    /**
     * 주문 추가 API
     * */

    public ResponseEntity<?> save(OrderEntity order) {
        if (order != null) {
            OrderEntity savedOrder = orderRepository.save(order);
            return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("주문한 아이템이 비어입니다.", HttpStatus.BAD_REQUEST);
        }
    }
    /**
     * 주문에 상품을 추가하는 API
     * <br>PUT 메소드 이므로 업데이트라고 생각
     * */

    public ResponseEntity<?> updateOrder( Long orderId) {
        Optional<OrderEntity> findOrder = orderRepository.findById(orderId);
        if (findOrder.isPresent()) {
            OrderEntity existingOrder = findOrder.get();
            orderRepository.save(existingOrder);
            return new ResponseEntity<>(existingOrder, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("주문 정보 업데이트에 실패했습니다", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * 단건 조회
     * */
    public ResponseEntity<OrderResponse> getOneOrder(Long orderId) {
        Optional<OrderEntity> optionalOrder = orderRepository.findById(orderId);
        return optionalOrder.map(order -> {
                    List<Long> productIds = order.getProductIds()
                            .stream()
                            .map(OrderMappingProductEntity::getProductId)
                            .collect(Collectors.toList());
                    OrderResponse response = new OrderResponse(order.getOrderId(), productIds);
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
