package com.mockproject.util;

import javax.servlet.http.HttpSession;

import com.mockproject.constant.SessionConst;
import com.mockproject.dto.CartDto;

public class SessionUtils {

	public static void validateCart(HttpSession session) {
		CartDto currentCart = (CartDto) session.getAttribute(SessionConst.CURRENT_CART);
		if (currentCart == null ) {
			session.setAttribute(SessionConst.CURRENT_CART, new CartDto());
		}
	}
	
	public static CartDto getCurrentCart(HttpSession session) {
		return (CartDto) session.getAttribute(SessionConst.CURRENT_CART);
	}
}
