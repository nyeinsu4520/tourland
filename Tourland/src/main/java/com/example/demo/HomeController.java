package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Controller
public class HomeController {
	

	

	
	
	@GetMapping("/403")
	public String error403() {
		return "403";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}

}
