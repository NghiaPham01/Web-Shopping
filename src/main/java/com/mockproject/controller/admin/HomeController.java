package com.mockproject.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller(value = "HomeControllerOfAdmin")
@RequestMapping("/admin")
public class HomeController {
	
	@GetMapping("/index")
	public String doGetIndex() {
		return "admin/index";
	}
}
