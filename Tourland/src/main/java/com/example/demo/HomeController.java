package com.example.demo;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
	
	@Autowired
    private CardRepository cardRepo;
	@Autowired
    private RestaurantRespository RestaurantRepo;
	@Autowired
	private PackageRepository packageRepo;
	

    @Autowired
    private CardService cardService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private ReviewPackageRepository reviewpackRepo;
    @Autowired
    private TripPackageRepository trippackageRepo;
    @Autowired 
    private ReviewRepository reviewhotelRepo;
    @Autowired
    private ReviewRestaurantRepository reviewrestaurantRepo;
    @Autowired
    private RestaurantService restaurantservice;
    @Autowired
    private ReviewTripPackageRepository reviewTripPackageRepo;
	

	
	
	@GetMapping("/403")
	public String error403() {
		return "403";
	}
	
	@GetMapping("/login")
	public String login() {
		return "Loginform";
	}
	
	
	 @GetMapping("/logout")
	    public String logout(HttpServletRequest request, HttpServletResponse response) {
	        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	        if (authentication != null) {
	            new SecurityContextLogoutHandler().logout(request, response, authentication);
	        }
	        return "redirect:/signout"; // Redirect to signout.html
	    }
	
	 @GetMapping("/signout")
	 public String signoutPage() {
	     return "signout"; // Return the name of the Thymeleaf template (signout.html)
	 }
	
	
	@GetMapping("/")
	public String index(Model model, Principal principal) {
	    List<Card> cardlist = cardRepo.findAll();
	    List<Restaurant> restaurantlist = RestaurantRepo.findAll();
	    List<Package> packagelist = packageRepo.findAll();
	    List<ReviewPackage> reviewpackagelist = reviewpackRepo.findAll();
	    List<TripPackage> tripPackages = trippackageRepo.findAll();
	    List<ReviewHotel> reviewhotel = reviewhotelRepo.findAll();
	    List<ReviewTripPackage> reviewTripPackage = reviewTripPackageRepo.findAll();
	    
	    
	  

	    // Calculate average ratings for packages
	    Map<Integer, Double> averageRatings = new HashMap<>();
	    for (Package pkg : packagelist) {
	        double avgRating = pkg.getReviewspackage().stream()
	            .mapToInt(ReviewPackage::getRating)
	            .average()
	            .orElse(0.0);
	        averageRatings.put(pkg.getPackage_id(), avgRating);
	    }

	    // Calculate average ratings for hotels
	    Map<Integer, Double> averageRatingshotel = new HashMap<>();
	    for (Card card : cardlist) {
	        double avgRating = card.getReviewshotel().stream()
	            .mapToInt(ReviewHotel::getRating)
	            .average()
	            .orElse(0.0);
	        averageRatingshotel.put(card.getHotel_id(), avgRating);
	    }

	 // Calculate average ratings for trip packages
	    Map<Long, Double> averageRatingsTripPackage = new HashMap<>();
	    for (TripPackage trippackage : tripPackages) {
	        double avgRating = trippackage.getReviewsTripPackage().stream()
	            .mapToInt(ReviewTripPackage::getRating)
	            .average()
	            .orElse(0.0);
	        averageRatingsTripPackage.put(trippackage.getId(), avgRating);
	    }
	    
	    // Calculate average ratings for restaurant
	    Map<Integer, Double> averageRatingsrestaurant = new HashMap<>();
	    for (Restaurant restaurant : restaurantlist) {
	        double avgRating = restaurant.getReviewsrestaurant().stream()
	            .mapToInt(ReviewRestaurant::getRating)
	            .average()
	            .orElse(0.0);
	        averageRatingsrestaurant.put(restaurant.getRestaurant_id(), avgRating);
	    }

	    
        
        if (principal != null) {
            Member member = memberService.findByUsername(principal.getName());
            model.addAttribute("userPrincipal", member);
        }
	    
        
	    model.addAttribute("cardlist", cardlist);
	    model.addAttribute("restaurantlist", restaurantlist);
	    model.addAttribute("packagelist", packagelist);
	    model.addAttribute("reviewpackagelist", reviewpackagelist);
	    model.addAttribute("averageRatings", averageRatings);
	    model.addAttribute("reviewhotel", reviewhotel);
	    model.addAttribute("averageRatingshotel", averageRatingshotel);
	    model.addAttribute("tripPackages", tripPackages);
	    model.addAttribute("averageRatingsTripPackage", averageRatingsTripPackage);
	    model.addAttribute("averageRatingsrestaurant", averageRatingsrestaurant);
	    return "index";
	}

}
