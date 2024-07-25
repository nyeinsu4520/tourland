package com.example.demo;


	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Controller;
	import org.springframework.ui.Model;
	import org.springframework.web.bind.annotation.GetMapping;
	import org.springframework.web.bind.annotation.PostMapping;
	import org.springframework.web.bind.annotation.RequestParam;
	import org.springframework.web.servlet.mvc.support.RedirectAttributes;


	@Controller
	public class SubscriptionController {

	    @Autowired
	    private SubscriptionRepository subscriptionRepository;

	    @Autowired
	    private SubscriptionService subscriptionService;

	    @GetMapping("/subscribe")
	    public String showSubscriptionForm() {
	        return "subscribe"; // Make sure this is the view that contains your form
	    }

	    @PostMapping("/subscribe")
	    public String subscribe(@RequestParam("email") String email, RedirectAttributes redirectAttributes) {
	        // Validate email
	        if (email == null || email.isEmpty()) {
	            redirectAttributes.addFlashAttribute("error", "Email address cannot be empty");
	            return "redirect:/subscribe"; // Redirect back to the form with error message
	        }

	        // Save subscription to the database
	        Subscription subscription = new Subscription(email);
	        subscriptionRepository.save(subscription);

	        redirectAttributes.addFlashAttribute("message", "Subscription successful! Thank you for subscribing.");
	        return "redirect:/subscribe"; // Redirect back to the form with success message
	    }
	}
