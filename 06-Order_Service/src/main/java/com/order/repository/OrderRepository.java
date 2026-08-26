package com.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.order.entity.OrderEntity;
import java.util.List;


public interface OrderRepository extends JpaRepository<OrderEntity, Long> 
{

	public  List<OrderEntity> findByUserId(Long userId);
}
