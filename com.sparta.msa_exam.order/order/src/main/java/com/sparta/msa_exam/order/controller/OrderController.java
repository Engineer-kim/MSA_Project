package com.sparta.msa_exam.order.controller;

import com.sparta.msa_exam.order.entity.OrderEntity;
import com.sparta.msa_exam.order.dto.OrderResponse;
import com.sparta.msa_exam.order.entity.OrderMappingProductEntity;
import com.sparta.msa_exam.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     *  주문 추가 API
     *  */
    @PostMapping("/order")
    public ResponseEntity<OrderEntity> createOrder(@RequestBody OrderEntity order) {
        return orderService.save(order);
    }



    /**
     * 주문에 상품을 추가하는 API
     * */
    @PutMapping("/order/{orderId}")
    public ResponseEntity<String> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody Map<String, Long> requestBody) {
        Long productId = requestBody.get("product_id");
        return orderService.updateOrder(orderId, productId);
    }

    /**
     * 주문 단건 조회 API
     * */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<OrderResponse> getOneOrder(@PathVariable("orderId") Long orderId) {
        OrderResponse orderResponse = orderService.getOneOrder(orderId);
        if (orderResponse != null) {
            return new ResponseEntity<>(orderResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


}
