package com.sparta.msa_exam.order.service;

import com.sparta.msa_exam.order.dto.OrderResponse;
import com.sparta.msa_exam.order.entity.OrderEntity;
import com.sparta.msa_exam.order.entity.OrderMappingProductEntity;
import com.sparta.msa_exam.order.feignClient.OrderServiceClient;
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
    private final OrderServiceClient orderServiceClient;

    @Transactional
    public ResponseEntity<OrderEntity> save(OrderEntity order) {
        if (order != null) {
            if (order.getProductList() != null) {
                for (OrderMappingProductEntity product : order.getProductList()) {
                    product.setOrderEntity(order);
                }
            }
            orderRepository.save(order);
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
            //Order_Mapping_product 테이블에 중복된 상품이 있는지 체크(API 테스트시 중복된 데이터 안들어가게하려고 자체적으로 만듦)
            boolean productExists = existingOrder.getProductIds().contains(productId);
            if (productExists) {
                return new ResponseEntity<>("해당 상품이 이미 주문에 존재합니다", HttpStatus.BAD_REQUEST);
            }
            // ProductServiceClient를 통해 프로덕트 아이디 체크
            // ==> (테이블 입장에서, 프로덕트 테이블의 프로덕트 아이디와 오더테이블의 프로덕트 아이디와 비교.)
            // 비교후 맞으면 저장(== 오더 테이블에 정보 저장) 아니면 저장안되게 튕구기
            ResponseEntity<List<Long>> response = orderServiceClient.fetchAllProductId();
            if (response.getStatusCode() == HttpStatus.OK) {
                List<Long> validProductIds = response.getBody();
                if (validProductIds != null) {
                    boolean productIdExists = validProductIds.stream()
                            .anyMatch(validProductId -> validProductId.equals(productId));
                    if (productIdExists) {
                        OrderMappingProductEntity product = new OrderMappingProductEntity();
                        product.setProductId(productId);
                        product.setOrderEntity(existingOrder);
                        //Order 테이블과 관련되있는 Order_mapping_product 테이블에 저장
                        existingOrder.addProduct(product);
                        //최종적으로 저장
                        orderRepository.save(existingOrder);
                        return new ResponseEntity<>("주문에 상품이 추가 되었습니다", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>("Product ID 가 상품목록테이블에 없습니다", HttpStatus.BAD_REQUEST);
                    }
                } else {
                    return new ResponseEntity<>("상품 정보를 가져오는 데 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                return new ResponseEntity<>("FeignClient 로직 오류", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>("해당 OrderID 로 찿은 결과가 없습니다(OrderID 테이블에서 재확인 요망)", HttpStatus.BAD_REQUEST);
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
