package com.mockproject.service.impl;

import java.util.HashMap;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mockproject.dto.CartDetailDto;
import com.mockproject.dto.CartDto;
import com.mockproject.entity.Orders;
import com.mockproject.entity.Products;
import com.mockproject.entity.Users;
import com.mockproject.service.CartService;
import com.mockproject.service.OrderDetailsService;
import com.mockproject.service.OrdersService;
import com.mockproject.service.ProductsService;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private ProductsService productService;
	
	@Autowired
	private OrdersService orderService;
	
	@Autowired
	private OrderDetailsService orderDetailService;

	@Override
	public CartDto updateCart(CartDto cart, Long productId, Integer quantity, boolean isReplace) {
		Products product = productService.findById(productId);
		HashMap<Long, CartDetailDto> listDetail = cart.getListDetail();
		
		// 1- them moi sp
		// 2- update: 2.1- cong don sp; 2.2- thay the bang SL moi (thay hoan toan)
		// 3- delete: khi update theo SL sp ve 0 -> tuong duong delete
		
		if (!listDetail.containsKey(productId)) {
			CartDetailDto cartDetail = new CartDetailDto();
			cartDetail.setProductId(productId);
			cartDetail.setName(product.getName());
			cartDetail.setPrice(product.getPrice());
			cartDetail.setQuantity(quantity);
			cartDetail.setSlug(product.getSlug());
			cartDetail.setImgUrl(product.getImgUrl());
			listDetail.put(productId, cartDetail);
		} else if (quantity > 0) {
			// thuc hien code update
			if (isReplace == true) { // neu thay the hoan toan:
				listDetail.get(productId).setQuantity(quantity);
			} else { // cong don
				Integer oldQuantity = listDetail.get(productId).getQuantity();
				Integer newQuantity = oldQuantity + quantity;
				listDetail.get(productId).setQuantity(newQuantity);
			}
		} else {
			// delete
			listDetail.remove(productId);
		}
		cart.setTotalQuantity(getTotalQuantity(cart));
		cart.setTotalPrice(getTotalPrice(cart));
		return cart;
	}
	
	@Override
	public Integer getTotalQuantity(CartDto cart) {
		Integer totalQuantity = 0;
		HashMap<Long, CartDetailDto> listDetail = cart.getListDetail();
		for (CartDetailDto detail : listDetail.values()) {
			totalQuantity += detail.getQuantity();
		}
		return totalQuantity;
	}

	@Override
	public Double getTotalPrice(CartDto cart) {
		Double totalPrice = 0D;
		HashMap<Long, CartDetailDto> listDetail = cart.getListDetail();
		for (CartDetailDto detail : listDetail.values()) {
			totalPrice += detail.getQuantity() * detail.getPrice();
		}
		return totalPrice;
	}

	@Transactional
	@Override
	public void insert(CartDto cart, Users user, String address, String phone) throws Exception {
		Orders order = new Orders();
		order.setUser(user);
		order.setAddress(address);
		order.setPhone(phone);
		try {
			Orders orderResponse = orderService.insert(order); // insert ORDERS
			
			for (CartDetailDto cartDetail : cart.getListDetail().values()) {
				// insert ORDER_DETAILS
				cartDetail.setOrderId(orderResponse.getId());
				orderDetailService.insert(cartDetail);
				
				// update quantity
				Products product = productService.findById(cartDetail.getProductId());
				Integer newQuantity = product.getQuantity() - cartDetail.getQuantity();
				productService.updateQuantity(newQuantity, cartDetail.getProductId());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new Exception("cannot insert cart to db");
		}
	}
}
