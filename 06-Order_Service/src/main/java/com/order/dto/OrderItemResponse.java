package com.order.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse 
{
	private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}
