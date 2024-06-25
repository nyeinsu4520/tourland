package com.example.demo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class BookingController {

	
	@Autowired
    private BookingRepository bookingPackageRepo;
	@Autowired 
	private PackageRepository packageRepo;
	@Autowired
	private bookingservice bookingservice;
	@Autowired
    private MemberService memberService;

	
	@GetMapping("/booking/submit")
	public String viewCategories(Model model) {
		
		List<Booking> bookingpackageList=bookingPackageRepo.findAll();// select *from category
		model.addAttribute("bookingpackageList",bookingpackageList);
		return "bookingConfirmation";
	}
	
	
	
	
	@PostMapping("/bookings/submit")
	public ModelAndView submitBooking(
	        @RequestParam("name") String name,
	        @RequestParam("email") String email,
	        @RequestParam("country") String country,
	        @RequestParam("phone") String phone,
	        @RequestParam("notes") String notes,
	        @RequestParam("selectedDate") String selectedDate,
	        @RequestParam("selectedPassengers") int selectedPassengers, 
	        @RequestParam("packageId") int packageId) {

	    // Retrieve the Package entity using the packageId
	    Package pkg = packageRepo.findById(packageId)
	                              .orElseThrow(() -> new IllegalArgumentException("Invalid packageId: " + packageId));
	    
	    
	    // Get logged-in member details
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberService.findByUsername(username);

	    // Create a new Booking object and populate it
	    Booking booking = new Booking();
	    booking.setMember(member);
	    booking.setName(name);
	    booking.setEmail(email);
	    booking.setCountry(country);
	    booking.setPhone(phone);
	    booking.setNotes(notes);
	    booking.setSelectedDate(LocalDate.parse(selectedDate));
	    booking.setSelectedPassengers(selectedPassengers);
	    booking.setPkg(pkg); // Set the Package entity

	    // Save the booking using the service
	    bookingservice.save(booking);

	    return new ModelAndView("bookingConfirmation");
	}
	
	



}
