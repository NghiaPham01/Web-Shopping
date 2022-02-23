package com.mockproject.controller;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mockproject.constant.SessionConst;
import com.mockproject.entity.Products;
import com.mockproject.entity.Users;
import com.mockproject.service.ProductsService;
import com.mockproject.service.UsersService;

@Controller
public class HomeController {

	@Autowired
	private ProductsService productService;

	@Autowired
	private UsersService userService;

	// localhost:8080/index
	@GetMapping("/index")
	public String doGetIndex(Model model) {
		List<Products> products = productService.findAllAvailable();
		model.addAttribute("products", products);
		return "user/index";
	}

	@GetMapping("/login")
	public String doGetLogin(Model model) {
		model.addAttribute("userRequest", new Users());
		return "user/login";
	}

	@GetMapping("/logout")
	public String doGetLogout(HttpSession session) {
		session.removeAttribute(SessionConst.CURRENT_USER);
		return "redirect:/index";
	}

	@GetMapping("/register")
	public String doGetRegister(Model model) {
		model.addAttribute("userRegister", new Users());
		return "user/register";
	}

	@PostMapping("/login")
	public String doPostLogin(@ModelAttribute("userRequest") Users userRequest, HttpSession session) {
		Users userResponse = userService.doLogin(userRequest.getUsername(), userRequest.getHashPassword());
		if (userResponse != null) {
			// dang nhap thanh cong
			session.setAttribute(SessionConst.CURRENT_USER, userResponse);
			return "redirect:/index";
		} else {
			// dang nhap that bai
			return "redirect:/login";
		}
	}

	@PostMapping("/register")
	public String doPostRegister(@Valid @ModelAttribute("userRegister") Users userRegister,
			BindingResult errors, HttpSession session) {
		Users userResponse = null;

		try {
			if (errors.hasErrors()) {
				List<FieldError> fieldErrors = errors.getFieldErrors();
				fieldErrors.forEach(error -> System.out.println(error.getField() + " - " + error.getDefaultMessage()));
				return "redirect:/register";
			} else {
				userResponse = userService.save(userRegister);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Register failed - Exception");
		}
		
		if (userResponse != null) {
			// dang ky thanh cong
			session.setAttribute(SessionConst.CURRENT_USER, userResponse);
			return "redirect:/index";
		} else {
			// dang ky that bai
			return "redirect:/register";
		}
	}
}
