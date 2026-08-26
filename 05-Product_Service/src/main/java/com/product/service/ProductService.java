package com.product.service;

import java.util.List;
import com.product.dto.ProductRequest;
import com.product.dto.ProductResponse;

public interface ProductService 
{
	 public ProductResponse createProduct(ProductRequest request);
	 
	 public ProductResponse getProductById(Long id);
	 
	 public List<ProductResponse> getAllProducts();
	 
	 public ProductResponse updateProduct(Long id, ProductRequest request);
	 
	 public void deleteProduct(Long id);
	 
	 public List<ProductResponse> getProductsByCategory(String category);
	 
	
}
