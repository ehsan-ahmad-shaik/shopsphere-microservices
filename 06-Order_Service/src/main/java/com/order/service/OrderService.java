package com.order.service;

import java.util.List;
import com.order.dto.OrderRequest;
import com.order.dto.OrderResponse;

public interface OrderService 
{

	public OrderResponse createOrder(OrderRequest request);
	
	public OrderResponse getOrderById(Long id);
	
	public List<OrderResponse> getAllOrders();
	
	public List<OrderResponse> getOrdersByUserId(Long userId);
	
	public OrderResponse updateOrderStatus(Long id, String status);
	
	public OrderResponse cancelOrder(Long id);
	
	public void deleteOrder(Long id);
	
}
