package com.sparta.msa_exam.product.entity;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long productId;
   private String name;
   private Integer supplyPrice;
}