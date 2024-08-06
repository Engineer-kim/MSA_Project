package com.sparta.msa_exam.product.entity;


import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class ProductEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long productId;
   private String name;
   private Integer supplyPrice;
}