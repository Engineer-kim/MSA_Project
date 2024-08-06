package com.sparta.msa_exam.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
public class OrderResponse implements Serializable {
    private Long orderId;
    private List<Integer> productIds;
}