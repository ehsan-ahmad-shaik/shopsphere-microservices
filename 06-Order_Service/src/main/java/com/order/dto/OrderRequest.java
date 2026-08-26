package com.order.dto;


import java.time.LocalDateTime;
import java.util.List;
import com.order.entity.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @NotNull(message = "User ID must not be null")
    @Positive(message = "User ID must be greater than 0")
    private Long userId;


    @NotNull(message = "Order status must not be null")
    private OrderStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NotEmpty(message = "Order must contain at least one item")
    @Valid
    private List<OrderItemRequest> items;
}