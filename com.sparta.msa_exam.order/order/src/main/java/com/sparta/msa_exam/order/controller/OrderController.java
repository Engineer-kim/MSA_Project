package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.entity.OrderEntity;
import com.sparta.msa_exam.order.dto.OrderResponse;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     *  주문 추가 API
     *  */
    @PostMapping("/order")
    public ResponseEntity<?> createOrder(@RequestBody OrderEntity order) {
        return orderService.save(order);
    }

    /**
     * 주문에 상품을 추가하는 API
     * */
    @Cacheable(cacheNames = "orders", key = "#orderId")
    @PutMapping("/order/{orderId}")
    public ResponseEntity<?> updateOrder(@PathVariable("orderId") Long orderId) {
        return orderService.updateOrder(orderId);
    }


    /**
     * 주문 단건 조회 API
     * */
    @Cacheable(cacheNames = "orders", key = "#orderId")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> getOneOrder(@PathVariable("orderId") Long orderId) {
        return orderService.getOneOrder(orderId);
    }


}
