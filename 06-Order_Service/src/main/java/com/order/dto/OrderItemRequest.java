package com.order.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

    @NotNull(message = "Product ID must not be null")
    @Positive(message = "Product ID must be greater than 0")
    private Long productId;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;
//
//    @NotNull(message = "Price must not be null")
//    @DecimalMin(
//        value = "0.01",
//        message = "Price must be greater than 0"
//    )
//    private BigDecimal price;
//
//    @NotNull(message = "Subtotal must not be null")
//    @DecimalMin(
//        value = "0.01",
//        message = "Subtotal must be greater than 0"
//    )
//    private BigDecimal subtotal;
}