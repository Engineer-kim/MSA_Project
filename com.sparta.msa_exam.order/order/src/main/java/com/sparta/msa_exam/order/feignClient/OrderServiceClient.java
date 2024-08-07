package com.sparta.msa_exam.order.feignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@FeignClient(name = "product-service", url = "http://localhost:19093")
public interface OrderServiceClient {
    @GetMapping("/getProductId")
    ResponseEntity<List<Long>> fetchAllProductId();
}
