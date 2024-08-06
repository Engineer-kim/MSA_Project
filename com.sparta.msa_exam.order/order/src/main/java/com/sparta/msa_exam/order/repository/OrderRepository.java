package com.sparta.msa_exam.order.repository;

import com.sparta.msa_exam.order.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Long> {
}
