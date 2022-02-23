package com.mockproject.service;

import com.mockproject.dto.CartDto;
import com.mockproject.entity.Users;

public interface CartService {

	// update: gom 3 truong hop them moi & cap nhat SL & delete
	CartDto updateCart(CartDto cart, Long productId, Integer quantity, boolean isReplace);
	Integer getTotalQuantity(CartDto cart);
	Double getTotalPrice(CartDto cart);
	void insert(CartDto cart, Users user, String address, String phone) throws Exception;
}
