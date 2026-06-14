package com.major.pgmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublicController {

	@GetMapping("/")
	public String home() {
		return "public/index";
	}

	@GetMapping("/about")
	public String about() {
		return "public/about";
	}

	@GetMapping("/features")
	public String features() {
		return "public/features";
	}

	@GetMapping("/contact")
	public String contact() {
		return "public/contact";
	}
}
