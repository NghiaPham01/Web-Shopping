package com.mockproject.controller.admin;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mockproject.entity.Users;
import com.mockproject.service.UsersService;

@Controller
@RequestMapping("/admin/user")
public class UserController {
	
	@Autowired
	private UsersService userService;
	
	@GetMapping("")
	public String doGetUser(Model model) {
		List<Users> users = userService.findAll();
		model.addAttribute("users", users);
		model.addAttribute("userRequest", new Users());
		return "admin/user";
	}
	
	// /admin/user/delete?username={}
	@GetMapping("/delete")
	public String doGetDelete(@RequestParam("username") String username,
			RedirectAttributes redirectAttributes) {
		try {
			userService.deleteLogical(username);
			redirectAttributes.addFlashAttribute("succeedMessage", "User " + username + " has been deleted");
		} catch (Exception ex) {
			ex.printStackTrace();
			redirectAttributes.addFlashAttribute("errorMessage", "Cannot delete user " + username);
		}
		return "redirect:/admin/user";
	}
	
	// /admin/user/edit?username={}
	@GetMapping("/edit")
	public String doGetEdit(@RequestParam("username") String username,
			Model model) {
		Users userRequest = userService.findByUsername(username);
		model.addAttribute("userRequest", userRequest);
		return "admin/user::#form";
	}
	
	@PostMapping("/create")
	public String doPostCreate(@Valid @ModelAttribute("userRequest") Users userRequest,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		String errorMessage = null;
		try {
			if (bindingResult.hasErrors()) {
				errorMessage = "User is not valid";
				List<FieldError> fieldErrors = bindingResult.getFieldErrors();
				fieldErrors.forEach(error -> System.out.println(error.getField() + " - " + error.getDefaultMessage()));
			} else {
				userService.save(userRequest);
				redirectAttributes.addFlashAttribute("succeedMessage", "User " + userRequest.getUsername() + " has been created");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			errorMessage = "Cannot create user";
		}
		
		if (!ObjectUtils.isEmpty(errorMessage)) {
			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
		}
		return "redirect:/admin/user";
	}
	
	@PostMapping("/edit")
	public String doPostUpdate(@Valid @ModelAttribute("userRequest") Users userRequest,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		String errorMessage = null;
		try {
			if (bindingResult.hasErrors()) {
				errorMessage = "User is not valid";
				List<FieldError> fieldErrors = bindingResult.getFieldErrors();
				fieldErrors.forEach(error -> System.out.println(error.getField() + " - " + error.getDefaultMessage()));
			} else {
				userService.update(userRequest);
				redirectAttributes.addFlashAttribute("succeedMessage", "User " + userRequest.getUsername() + " has been updated");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			errorMessage = "Cannot update user";
		}
		
		if (!ObjectUtils.isEmpty(errorMessage)) {
			redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
		}
		return "redirect:/admin/user";
	}
}
