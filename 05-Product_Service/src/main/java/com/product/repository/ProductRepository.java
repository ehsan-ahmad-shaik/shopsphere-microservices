package com.product.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.product.entity.ProductEntity;

public interface ProductRepository extends JpaRepository<ProductEntity, Long>
{
	public List<ProductEntity> findByCategory(String category);
}
