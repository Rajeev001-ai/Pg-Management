package com.major.pgmanagement.controller;

import com.major.pgmanagement.entity.User;
import com.major.pgmanagement.entity.enums.AccountStatus;
import com.major.pgmanagement.entity.enums.Role;
import com.major.pgmanagement.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

	private final UserService userService;
	private final PasswordEncoder passwordEncoder;

	public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/login")
	public String loginPage() {
		return "auth/login";
	}

	@GetMapping("/register")
	public String registerPage(@RequestParam(defaultValue = "tenant") String type, Model model) {
		if (!model.containsAttribute("user")) {
			model.addAttribute("user", new User());
		}
		model.addAttribute("registrationType", "owner".equalsIgnoreCase(type) ? "owner" : "tenant");
		return "auth/register";
	}

	@PostMapping("/register")
	public String registerUser(
			@ModelAttribute("user") User user,
			BindingResult bindingResult,
			@RequestParam(defaultValue = "tenant") String registrationType,
			Model model,
			RedirectAttributes redirectAttributes) {

		boolean ownerRegistration = "owner".equalsIgnoreCase(registrationType);
		validateRegistration(user, bindingResult, ownerRegistration);

		if (bindingResult.hasErrors()) {
			model.addAttribute("registrationType", ownerRegistration ? "owner" : "tenant");
			return "auth/register";
		}

		user.setRole(ownerRegistration ? Role.OWNER : Role.TENANT);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setEnabled(!ownerRegistration);
		user.setAccountStatus(ownerRegistration ? AccountStatus.PENDING_APPROVAL : AccountStatus.ACTIVE);

		try {
			userService.registerUser(user);
		} catch (RuntimeException ex) {
			bindingResult.addError(new FieldError("user", "email", "Email is already registered."));
			model.addAttribute("registrationType", ownerRegistration ? "owner" : "tenant");
			return "auth/register";
		}

		if (ownerRegistration) {
			redirectAttributes.addFlashAttribute("success", "Owner registration submitted. Please wait for admin approval before login.");
		} else {
			redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
		}
		return "redirect:/login";
	}

	private void validateRegistration(User user, BindingResult bindingResult, boolean ownerRegistration) {
		if (!StringUtils.hasText(user.getFullName())) {
			bindingResult.addError(new FieldError("user", "fullName", "Full name is required."));
		}
		if (!StringUtils.hasText(user.getEmail())) {
			bindingResult.addError(new FieldError("user", "email", "Email is required."));
		}
		if (!StringUtils.hasText(user.getPhone())) {
			bindingResult.addError(new FieldError("user", "phone", "Phone is required."));
		}
		if (!StringUtils.hasText(user.getPassword())) {
			bindingResult.addError(new FieldError("user", "password", "Password is required."));
		} else if (user.getPassword().length() < 6) {
			bindingResult.addError(new FieldError("user", "password", "Password must be at least 6 characters."));
		}
		if (ownerRegistration) {
			if (!StringUtils.hasText(user.getPgName())) {
				bindingResult.addError(new FieldError("user", "pgName", "PG name is required."));
			}
			if (!StringUtils.hasText(user.getPgAddress())) {
				bindingResult.addError(new FieldError("user", "pgAddress", "PG address is required."));
			}
		}
	}
}
