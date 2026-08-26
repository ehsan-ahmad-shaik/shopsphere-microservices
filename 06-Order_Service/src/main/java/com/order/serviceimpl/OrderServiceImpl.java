package com.order.serviceimpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import com.order.client.ProductClient;
import com.order.client.UserClient;
import com.order.client.dto.Product;
import com.order.client.dto.User;
import com.order.dto.OrderItemRequest;
import com.order.dto.OrderRequest;
import com.order.dto.OrderResponse;
import com.order.entity.OrderEntity;
import com.order.entity.OrderItem;
import com.order.entity.OrderStatus;
import com.order.exception.OrderNotFoundException;
import com.order.exception.ProductNotFoundException;
import com.order.exception.QuantityMustBeLessThanStockException;
import com.order.exception.UserNotFoundException;
import com.order.repository.OrderRepository;
import com.order.service.OrderService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	private final ModelMapper modelMapper;

	private final ProductClient productClient;

	private final UserClient userClient;

	private final CircuitBreakerFactory<?, ?> circuitBreakerFactory;

	// ============================================================
	// CREATE ORDER
	// ============================================================

	@Override
	public OrderResponse createOrder(OrderRequest request) {

		// --------------------------------------------------------
		// 1. CHECK USER EXISTS
		// --------------------------------------------------------

		User userById = getUserById(request.getUserId());

		System.out.println("\nUser Details : " + userById + "\n");

		// --------------------------------------------------------
		// 2. CREATE ORDER
		// --------------------------------------------------------

		OrderEntity order = new OrderEntity();

		order.setUserId(request.getUserId());

		order.setStatus(request.getStatus());

		order.setCreatedAt(LocalDateTime.now());

		order.setUpdatedAt(LocalDateTime.now());

		// --------------------------------------------------------
		// 3. CREATE ORDER ITEMS
		// --------------------------------------------------------

		List<OrderItem> items = new ArrayList<>();

		BigDecimal totalAmount = BigDecimal.ZERO;

		// --------------------------------------------------------
		// 4. PROCESS EACH PRODUCT
		// --------------------------------------------------------

		for (OrderItemRequest itemRequest : request.getItems()) {

			Product product = getProductById(itemRequest.getProductId());

			System.out.println("\nProduct Details : " + product + "\n");

			// ----------------------------------------------------
			// CHECK PRODUCT ID
			// ----------------------------------------------------

			if (product.getId() == null || !product.getId().equals(itemRequest.getProductId())) {

				throw new ProductNotFoundException("Product not found by this id: " + itemRequest.getProductId());
			}

			// ----------------------------------------------------
			// CHECK PRODUCT QUANTITY
			// ----------------------------------------------------

			if (product.getQuantity() < itemRequest.getQuantity()) {

				throw new QuantityMustBeLessThanStockException("Requested quantity is greater than available "
						+ "product quantity. Available quantity: " + product.getQuantity());
			}

			// ----------------------------------------------------
			// GET PRICE
			// ----------------------------------------------------

			BigDecimal price = BigDecimal.valueOf(product.getPrice());

			// ----------------------------------------------------
			// GET REQUESTED QUANTITY
			// ----------------------------------------------------

			Integer quantity = itemRequest.getQuantity();

			// ----------------------------------------------------
			// CALCULATE SUBTOTAL
			// ----------------------------------------------------

			BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));

			// ----------------------------------------------------
			// CREATE ORDER ITEM
			// ----------------------------------------------------

			OrderItem orderItem = new OrderItem();

			orderItem.setProductId(product.getId());

			orderItem.setQuantity(quantity);

			orderItem.setPrice(price);

			orderItem.setSubtotal(subtotal);

			orderItem.setOrder(order);

			// Add item to list
			items.add(orderItem);

			// Add subtotal to total
			totalAmount = totalAmount.add(subtotal);
		}

		// --------------------------------------------------------
		// 5. SET ORDER ITEMS
		// --------------------------------------------------------

		order.setItems(items);

		// --------------------------------------------------------
		// 6. SET TOTAL AMOUNT
		// --------------------------------------------------------

		order.setTotalAmount(totalAmount);

		// --------------------------------------------------------
		// 7. SAVE ORDER
		// --------------------------------------------------------

		OrderEntity savedOrder = orderRepository.save(order);

		// --------------------------------------------------------
		// 8. RETURN RESPONSE
		// --------------------------------------------------------

		return convertToOrderResponse(savedOrder);
	}

	// ============================================================
	// GET USER BY ID - CIRCUIT BREAKER
	// ============================================================

	private User getUserById(Long userId) {

		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("userService");

		return circuitBreaker.run(

				() -> userClient.getUserById(userId)
						.orElseThrow(() -> new UserNotFoundException("User not found with this id: " + userId)),

				throwable -> {

					System.out.println("USER SERVICE DOWN : " + throwable.getMessage());

					throw new RuntimeException("User Service is currently unavailable");
				});
	}

	// ============================================================
	// GET PRODUCT BY ID - CIRCUIT BREAKER
	// ============================================================

	private Product getProductById(Long productId) {

		CircuitBreaker circuitBreaker = circuitBreakerFactory.create("productService");

		return circuitBreaker.run(

				() -> productClient.getProductById(productId)
						.orElseThrow(() -> new ProductNotFoundException("Product not found by this id: " + productId)),

				throwable -> {

					System.out.println("PRODUCT SERVICE DOWN : " + throwable.getMessage());

					throw new RuntimeException("Product Service is currently unavailable");
				});
	}

	// ============================================================
	// GET ORDER BY ID
	// ============================================================

	@Override
	public OrderResponse getOrderById(Long id) {

		OrderEntity order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order not found by this id: " + id));

		return convertToOrderResponse(order);
	}

	// ============================================================
	// GET ALL ORDERS
	// ============================================================

	@Override
	public List<OrderResponse> getAllOrders() {

		List<OrderEntity> orders = orderRepository.findAll();

		if (orders.isEmpty()) {

			return Collections.emptyList();
		}

		return orders.stream().map(this::convertToOrderResponse).toList();
	}

	// ============================================================
	// GET ORDERS BY USER ID
	// ============================================================

	@Override
	public List<OrderResponse> getOrdersByUserId(Long userId) {

		List<OrderEntity> orders = orderRepository.findByUserId(userId);

		if (orders.isEmpty()) {

			return Collections.emptyList();
		}

		return orders.stream().map(this::convertToOrderResponse).toList();
	}

	// ============================================================
	// UPDATE ORDER STATUS
	// ============================================================

	@Override
	public OrderResponse updateOrderStatus(Long id, String status) {

		OrderEntity order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order not found by this id: " + id));

		OrderStatus orderStatus;

		try {

			orderStatus = OrderStatus.valueOf(status.toUpperCase());

		} catch (IllegalArgumentException e) {

			throw new IllegalArgumentException("Invalid order status: " + status);
		}

		order.setStatus(orderStatus);

		order.setUpdatedAt(LocalDateTime.now());

		OrderEntity updatedOrder = orderRepository.save(order);

		return convertToOrderResponse(updatedOrder);
	}

	// ============================================================
	// CANCEL ORDER
	// ============================================================

	@Override
	public OrderResponse cancelOrder(Long id) {

		OrderEntity order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order not found by this id: " + id));

		order.setStatus(OrderStatus.CANCELLED);

		order.setUpdatedAt(LocalDateTime.now());

		OrderEntity cancelledOrder = orderRepository.save(order);

		return convertToOrderResponse(cancelledOrder);
	}

	// ============================================================
	// DELETE ORDER
	// ============================================================

	@Override
	public void deleteOrder(Long id) {

		OrderEntity order = orderRepository.findById(id)
				.orElseThrow(() -> new OrderNotFoundException("Order not found by this id: " + id));

		orderRepository.delete(order);
	}

	// ============================================================
	// ENTITY -> RESPONSE
	// ============================================================

	private OrderResponse convertToOrderResponse(OrderEntity orderEntity) {

		return modelMapper.map(orderEntity, OrderResponse.class);
	}
}