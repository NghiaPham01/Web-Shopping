package com.mockproject.dto;

import java.io.Serializable;
import java.util.HashMap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartDto implements Serializable {

	private static final long serialVersionUID = -3816551819932131145L;

	private Long userId;
	private String address;
	private String phone;
	private Integer totalQuantity = 0;
	private Double totalPrice = 0D;
	private HashMap<Long, CartDetailDto> listDetail = new HashMap<>();
}
