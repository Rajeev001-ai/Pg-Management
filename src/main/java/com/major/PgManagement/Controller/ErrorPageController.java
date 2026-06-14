package com.major.pgmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorPageController {

	@GetMapping("/error/access-denied")
	public String accessDenied() {
		return "error/access-denied";
	}
}
