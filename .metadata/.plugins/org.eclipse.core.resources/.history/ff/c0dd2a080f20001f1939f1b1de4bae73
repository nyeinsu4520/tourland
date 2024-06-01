package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
public class BookingController {

	
	@Autowired
    private BookingRepository bookingPackageRepo;
	@Autowired 
	private PackageRepository packageRepo;
	
	
	@GetMapping("/booking/package")
	public String viewCategories(Model model) {
		
		List<Booking> bookingpackageList=bookingPackageRepo.findAll();// select *from category
		model.addAttribute("bookingpackageList",bookingpackageList);
		return "booking";
	}
	
	
	
	
	@GetMapping("/booking/package/add")
	public String addBookingPackage(Model model) {
		model.addAttribute("booking",new Booking());
		List<Package> packagelist=packageRepo.findAll();
		model.addAttribute("packagelist",packagelist);
		return "Add_packagebooking";
	}
	
	@PostMapping("booking/save")
	public String saveItem(@Valid Booking booking,BindingResult bindingResult,Model model) {
		if(bindingResult.hasErrors()) {
			List<Package> packagelist=packageRepo.findAll();
			model.addAttribute("packagelist",packagelist);
			return "Add-packagebooking";
		}
		bookingPackageRepo.save(booking);
		return "redirect:/booking/package";
	}
}
