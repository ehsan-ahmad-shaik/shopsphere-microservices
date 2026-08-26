package com.order.exception;

public class QuantityMustBeLessThanStockException extends RuntimeException 
{

	public QuantityMustBeLessThanStockException(String message) {
		super(message);
	}
}
