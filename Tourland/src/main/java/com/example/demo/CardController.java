package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@Controller
public class CardController {

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
    



    @GetMapping("/index")
    public String getIndex(Model model) {
        List<Card> cardlist = cardRepo.findAll();
        List<Restaurant> restaurantlist = RestaurantRepo.findAll();
        List<Package> packagelist = packageRepo.findAll();
        List<ReviewPackage> reviewpackagelist = reviewpackRepo.findAll();

        // Calculate average ratings
        Map<Integer, Double> averageRatings = new HashMap<>();
        for (Package pkg : packagelist) {
            double avgRating = pkg.getReviewspackage().stream()
                .mapToInt(ReviewPackage::getRating)
                .average()
                .orElse(0.0);
            averageRatings.put(pkg.getPackage_id(), avgRating);
        }

        model.addAttribute("cardlist", cardlist);
        model.addAttribute("restaurantlist", restaurantlist);
        model.addAttribute("packagelist", packagelist);
        model.addAttribute("reviewpackagelist", reviewpackagelist);
        model.addAttribute("averageRatings", averageRatings);

        return "index";
    }

	
	 @GetMapping("/cards")
	    public String getAllHotels(Model model) {
	        List<Card> cards = cardService.findAll();
	        model.addAttribute("cards", cards);
	        return "hoteldetails";
	    }
	 
	 @PostMapping("/cards")
	    public String createHotel(@ModelAttribute Card card) {
	        cardService.save(card);
	        return "redirect:/cards";
	    }
	 
	 
	 
	 @GetMapping("/cards/review/{id}")
	 public String getCardById(@PathVariable Integer id, Model model, Principal principal) {
	     Optional<Card> cardOptional = cardService.findById(id);
	     if (cardOptional.isPresent()) {
	         Card card = cardOptional.get();
	         List<ReviewHotel> reviews = card.getReviewshotel(); // Fetch reviews associated with the card
	         
	         // Get the logged-in member's ID
	         String loggedInMemberId = "";
	         if (principal != null) {
	             // Retrieve the username from the principal
	             String username = principal.getName();
	             // Use the username to find the member ID
	             String memberId = memberService.findIdByUsername(username);
	             if (memberId != null) {
	                 loggedInMemberId = memberId;
	             }
	         }
	         
	         model.addAttribute("card", card);
	         model.addAttribute("reviews", reviews); // Add reviews to the model
	         model.addAttribute("loggedInMemberId", loggedInMemberId);
	     }
	     return "hoteldetails";
	 }

	 
	
	

	 @PostMapping("/cards/{id}/reviews")
	 public String addReview(@PathVariable Integer id, @RequestParam String comment, @RequestParam int rating, Principal principal) {
	     Optional<Card> cardOpt = cardService.findById(id);
	     if (cardOpt.isPresent() && principal != null) {
	         String username = principal.getName();
	         Member member = memberService.findByUsername(username); // Fetch the logged-in member
	         if (member != null) {
	             Card card = cardOpt.get();
	             ReviewHotel review = new ReviewHotel(comment, rating);
	             review.setMember(member); // Associate the comment with the logged-in member
	             review.setCard(card);
	             card.getReviewshotel().add(review);
	             cardService.save(card);
	         }
	     }
	     return "redirect:/cards/review/" + id;
	 }



	
	@GetMapping("/cards/add")
	public String addItem(Model model) {
		model.addAttribute("card",new Card());
		List<Card> cardlist=cardRepo.findAll();
		model.addAttribute("cardlist",cardlist);
		return "add_card";
	}
	
	
	@PostMapping("/cards/save")
	public String saveItem(@Valid Card card,BindingResult bindingResult, @RequestParam("cardImage") MultipartFile imgFile,@RequestParam double latitude, @RequestParam double longitude,Model model) {
	if(bindingResult.hasErrors()) {
		List<Card> cardlist=cardRepo.findAll();
		model.addAttribute("cardlist",cardlist);
		return "add_card";
	}
	
	String imageName = imgFile.getOriginalFilename();
	// set the image name in item object
	card.setImage(imageName);
	// save the item obj to the db
	Card savedItem = cardRepo.save(card);
	try {
	// prepare the directory path
	String uploadDir = "uploads/cards/" + savedItem.getHotel_id();
	Path uploadPath = Paths.get(uploadDir);
	// check if the upload path exists, if not it will be created
	if (!Files.exists(uploadPath)) {
	Files.createDirectories(uploadPath);
	}
	// prepare path for file
	Path fileToCreatePath = uploadPath.resolve(imageName);
	System.out.println("File path: " + fileToCreatePath);
	// copy file to the upload location
	Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
	} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	}
	cardRepo.save(card);
	return "redirect:/index";
	}
	
    
	
	@GetMapping("/restaurant/add")
	public String addRestaurant(Model model) {
		model.addAttribute("restaurant",new Restaurant());
		List<Restaurant> restaurantlist=RestaurantRepo.findAll();
		model.addAttribute("restaurantlist",restaurantlist);
		return "add_restaurant";
	}
		
		
	@PostMapping("/restaurant/save")
	public String saveRestaurant(@Valid Restaurant restaurant,BindingResult bindingResult, @RequestParam("restaurantImage") MultipartFile imgFile,Model model) {
	if(bindingResult.hasErrors()) {
		List<Restaurant> restaurantlist=RestaurantRepo.findAll();
		model.addAttribute("restaurantlist",restaurantlist);
		return "add_restaurant";
	}
		
	String imageName = imgFile.getOriginalFilename();
	// set the image name in item object
	restaurant.setImage(imageName);
	// save the item obj to the db
	Restaurant savedItem = RestaurantRepo.save(restaurant);
	try {
	// prepare the directory path
	String uploadDir = "uploads/restaurants/" + savedItem.getRestaurant_id();
	Path uploadPath = Paths.get(uploadDir);
	// check if the upload path exists, if not it will be created
	if (!Files.exists(uploadPath)) {
	Files.createDirectories(uploadPath);
	}
	// prepare path for file
	Path fileToCreatePath = uploadPath.resolve(imageName);
	System.out.println("File path: " + fileToCreatePath);
	// copy file to the upload location
	Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
	} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	}
	RestaurantRepo.save(restaurant);
	return "redirect:/index";
	}
    
	@GetMapping("/package/add")
	public String addPackage(Model model) {
		model.addAttribute("package",new Package());
		List<Package> packagelist=packageRepo.findAll();
		model.addAttribute("packagelist",packagelist);
		return "add_package";
	}
	
	
	@PostMapping("/package/save")
	public String savePackage(@Valid Package packageObj,BindingResult bindingResult, @RequestParam("packageImage") MultipartFile imgFile,Model model) {
	if(bindingResult.hasErrors()) {
		List<Package> packagelist=packageRepo.findAll();
		model.addAttribute("packagelist",packagelist);
		return "add_package";
	}
	
	String imageName = imgFile.getOriginalFilename();
	// set the image name in item object
	packageObj.setImage(imageName);
	// save the item obj to the db
	Package savedItem = packageRepo.save(packageObj);
	try {
	// prepare the directory path
	String uploadDir = "uploads/packages/" + savedItem.getPackage_id();
	Path uploadPath = Paths.get(uploadDir);
	// check if the upload path exists, if not it will be created
	if (!Files.exists(uploadPath)) {
	Files.createDirectories(uploadPath);
	}
	// prepare path for file
	Path fileToCreatePath = uploadPath.resolve(imageName);
	System.out.println("File path: " + fileToCreatePath);
	// copy file to the upload location
	Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
	} catch (IOException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
	}
	packageRepo.save(packageObj);
	return "redirect:/index";
	}
	
	@GetMapping("/package/review/{id}")
	public String getPackageById(@PathVariable Integer id, Model model, Principal principal) {
	    Optional<Package> packageOptional = packageService.findById(id);
	    if (packageOptional.isPresent()) {
	        Package packageObj = packageOptional.get();
	        List<ReviewPackage> reviewspackage = packageObj.getReviewspackage(); // Fetch package reviews
	        
	        // Get the logged-in member's ID
	        String loggedInMemberId = "";
	        if (principal != null) {
	            // Retrieve the username from the principal
	            String username = principal.getName();
	            // Use the username to find the member ID
	            String memberId = memberService.findIdByUsername(username);
	            if (memberId != null) {
	                loggedInMemberId = memberId;
	            }
	        }
	        
	        model.addAttribute("package", packageObj);
	        model.addAttribute("reviewspackage", reviewspackage); // Add reviews to the model
	        model.addAttribute("loggedInMemberId", loggedInMemberId);
	    }
	    return "packagerating";
	}

	 @PostMapping("/package/{id}/reviews")
	 public String addReviewpackage(@PathVariable Integer id, @RequestParam String comment, @RequestParam int rating, Principal principal) {
	     Optional<Package> packageOpt = packageService.findById(id);
	     if (packageOpt.isPresent() && principal != null) {
	         String username = principal.getName();
	         Member member = memberService.findByUsername(username); // Fetch the logged-in member
	         if (member != null) {
	             Package packageObj = packageOpt.get();
	             ReviewPackage reviewpackage = new ReviewPackage(comment, rating);
	             reviewpackage.setMember(member); // Associate the comment with the logged-in member
	             reviewpackage.setPackageObj(packageObj);
	             packageObj.getReviewspackage().add(reviewpackage);
	             packageService.save(packageObj);
	         }
	     }
	     return "redirect:/package/review/" + id;
	 }

	 
	 @GetMapping("/package/view/{id}")
	 public String viewsingle(@PathVariable("id") Integer id, Model model) {
	     Optional<Package> packageOpt = packageRepo.findById(id);
	     if (packageOpt.isPresent()) {
	         Package packageObj = packageOpt.get();
	         model.addAttribute("packageObj", packageObj);

	         // Calculate average rating
	         double avgRating = packageObj.getReviewspackage().stream()
	             .mapToInt(ReviewPackage::getRating)
	             .average()
	             .orElse(0.0);

	         // Get the number of reviews
	         int reviewCount = packageObj.getReviewspackage().size();

	         model.addAttribute("averageRating", avgRating);
	         model.addAttribute("reviewCount", reviewCount);

	         return "PackageDetails";
	     } else {
	         return "redirect:/error"; // Redirect or show error if package not found
	     }
	 }

	

}

    
	
	
	
    

