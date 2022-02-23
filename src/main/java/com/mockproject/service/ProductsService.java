package com.mockproject.service;

import java.util.List;

import com.mockproject.entity.Products;

public interface ProductsService {

	Products findById(Long id);
	List<Products> findAllAvailable();
	void updateQuantity(Integer quantity, Long productId);
}
