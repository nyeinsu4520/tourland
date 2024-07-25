package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class aboutuscontroller {

	@GetMapping("/aboutus")
	public String aboutus(Model model) {
		
		
		return "Aboutus";
	}
	


	@GetMapping("/information")
	public String information(Model model) {
		
		
		return "Information";
	}
}
