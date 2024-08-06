package com.sparta.msa_exam.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private List<Long> productIds;
}