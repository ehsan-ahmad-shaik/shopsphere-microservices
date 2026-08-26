package com.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.order.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse 
{

	private Long id;

    private Long userId;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private List<OrderItemResponse> items;
}
