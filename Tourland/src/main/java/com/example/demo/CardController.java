package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Autowired
    private TripPackageRepository trippackageRepo;
    @Autowired 
    private ReviewRepository reviewhotelRepo;
    @Autowired
    private ReviewRestaurantRepository reviewrestaurantRepo;
    @Autowired
    private RestaurantService restaurantservice;
   
    



    /*@GetMapping("/index")
    public String getIndex(Model model) {
        List<Card> cardlist = cardRepo.findAll();
        List<Restaurant> restaurantlist = RestaurantRepo.findAll();
        List<Package> packagelist = packageRepo.findAll();
        List<ReviewPackage> reviewpackagelist = reviewpackRepo.findAll();
        List<TripPackage> tripPackages = trippackageRepo.findAll();
        List<ReviewHotel> reviewhotel = reviewhotelRepo.findAll();

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

        model.addAttribute("cardlist", cardlist);
        model.addAttribute("restaurantlist", restaurantlist);
        model.addAttribute("packagelist", packagelist);
        model.addAttribute("reviewpackagelist", reviewpackagelist);
        model.addAttribute("averageRatings", averageRatings);
        model.addAttribute("reviewhotel", reviewhotel);
        model.addAttribute("averageRatingshotel", averageRatingshotel);
        model.addAttribute("tripPackages", tripPackages);
        return "redirect: /";
    }*/
	
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
	     return "hotel_details";
	 }

	 
	
	
		

		@PostMapping("/cards/{id}/reviews")
		public String addReviewcard(@PathVariable Integer id, 
		                                  @RequestParam String comment, 
		                                  @RequestParam int rating, 
		                                  Principal principal) {
		    Optional<Card> cardOptional = cardService.findById(id);
		    if (cardOptional.isPresent() && principal != null) {
		        String username = principal.getName();
		        Member member = memberService.findByUsername(username); // Fetch the logged-in member
		        if (member != null) {
		            Card card = cardOptional.get(); // Rename to avoid conflict
		            ReviewHotel reviewHotel = new ReviewHotel(comment, rating);
		            reviewHotel.setMember(member); // Associate the comment with the logged-in member
		            reviewHotel.setCard(card);
		            card.getReviewshotel().add(reviewHotel); // Add the review to the restaurant's list of reviews
		            cardService.save(card); // Save the restaurant entity to persist the review
		        }
		    }
		    return "redirect:/card/view/" + id;
		}

		

		@GetMapping("/cards/add")
		public String addItem(Model model) {
		    Card card = new Card();
		    card.getRoomTypes().add(new RoomType()); // Initialize with one RoomType

		    // Add the new Card instance to the model
		    model.addAttribute("card", card);

		    // Optionally, fetch and add all cards to the model
		    List<Card> cardList = cardRepo.findAll();
		    model.addAttribute("cardList", cardList);

		    return "add_card";
		}

		@PostMapping("/cards/save")
		public String saveItem(@ModelAttribute Card card, BindingResult bindingResult,
		                       @RequestParam("cardImage") MultipartFile imgFile,
		                       Model model, @RequestParam("roomImages") MultipartFile[] roomImages) throws IOException {
		    if (bindingResult.hasErrors()) {
		        List<Card> cardList = cardRepo.findAll();
		        model.addAttribute("cardList", cardList);
		        return "add_card";
		    }

		    // Set image name in card object
		    String imageName = imgFile.getOriginalFilename();
		    card.setImage(imageName);

		    // Save the card object to get the generated hotel_id
		    Card savedCard = cardRepo.save(card);

		    try {
		        // Prepare directory path
		        String uploadDir = "uploads/cards/" + savedCard.getHotel_id();
		        Path uploadPath = Paths.get(uploadDir);
		        // Check if upload path exists, create if not
		        if (!Files.exists(uploadPath)) {
		            Files.createDirectories(uploadPath);
		        }
		        // Prepare path for file
		        Path fileToCreatePath = uploadPath.resolve(imageName);
		        System.out.println("File path: " + fileToCreatePath);
		        // Copy file to upload location
		        Files.copy(imgFile.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }

		    // Save room type images and link them to card
		    for (int i = 0; i < card.getRoomTypes().size(); i++) {
		        RoomType roomType = card.getRoomTypes().get(i);
		        if (!roomImages[i].isEmpty()) {
		            byte[] bytes = roomImages[i].getBytes();
		            Path path = Paths.get("src/main/resources/static/images/" + roomImages[i].getOriginalFilename());
		            Files.write(path, bytes);
		            roomType.setImageFilename(roomImages[i].getOriginalFilename());
		        }
		        // Set the card reference in room type
		        roomType.setCard(savedCard);
		    }

		    // Update the card with updated room types
		    savedCard.setRoomTypes(card.getRoomTypes());

		    // Save the card again with updated room types and images
		    cardRepo.save(savedCard);

		    return "redirect:/viewcards";
		}


		@GetMapping("/viewhotel")
		public String viewHotels(Model model) {
			 List<Object[]> results = cardRepo.findAllWithAverageRatings();
			 // Convert the results to a list of Cards with their ratings
	        List<Card> cardList = results.stream()
	                .map(result -> {
	                    Card card = (Card) result[0];
	                    Double averageRating = (Double) result[1];
	                    card.setAverageRating(averageRating); // Assuming you have a setAverageRating method in your Card entity
	                    return card;
	                })
	                .collect(Collectors.toList());

	        // Sort the list by title and rating
	        cardList.sort((c1, c2) -> {
	            int titleCompare = c1.getTitle().compareToIgnoreCase(c2.getTitle());
	            if (titleCompare != 0) {
	                return titleCompare;
	            }
	            return c2.getAverageRating().compareTo(c1.getAverageRating());
	        });
		    model.addAttribute("cardList", cardList); // Add sorted cardList to model

		    return "ViewHotelFront"; // Return Thymeleaf template name
		}


	
	
		@GetMapping("/viewcards")
	    public String viewCards(Model model) {
	        List<Card> cardList = cardService.getAllCards(); // Assuming you fetch all cards
	        
	        model.addAttribute("cardList", cardList);
	        return "ViewHotel"; // Assuming your Thymeleaf template name is viewCards.html
	    }
	
	
		@GetMapping("card/edit/{id}")
		public String editCardForm(@PathVariable("id") Integer id, Model model) {
		    Optional<Card> cardOpt = cardRepo.findById(id);
		    if (cardOpt.isPresent()) {
		        model.addAttribute("card", cardOpt.get());
		        return "edit_card";
		    } else {
		        return "redirect:/viewcards"; // or handle the error appropriately
		    }
		}

		@PostMapping("/card/edit/{id}")
		public String editCard(@PathVariable("id") Integer id,
		                       @ModelAttribute("card") @Valid Card updatedCard,
		                       BindingResult bindingResult,
		                       @RequestParam("cardImage") MultipartFile cardImage,
		                       @RequestParam(value = "roomImages", required = false) MultipartFile[] roomImages,
		                       Model model) throws IOException {

		    if (bindingResult.hasErrors()) {
		        // Handle validation errors appropriately
		        model.addAttribute("card", updatedCard);
		        return "edit_card";
		    }

		    // Retrieve the existing card from the database
		    Optional<Card> existingCardOpt = cardRepo.findById(id);
		    if (!existingCardOpt.isPresent()) {
		        return "redirect:/viewcards"; // Handle if card is not found
		    }
		    Card existingCard = existingCardOpt.get();

		    // Update existing card's fields
		    existingCard.setTitle(updatedCard.getTitle());
		    existingCard.setDescription(updatedCard.getDescription());
		    existingCard.setLocation(updatedCard.getLocation());
		    existingCard.setAddress(updatedCard.getAddress());
		    existingCard.setPhnum(updatedCard.getPhnum());
		    existingCard.setHas_foreign_key_reference(updatedCard.getHas_foreign_key_reference());

		    // Update card's main image if provided
		    if (!cardImage.isEmpty()) {
		        String imageName = cardImage.getOriginalFilename();
		        // Save image processing logic here
		        existingCard.setImage(imageName);
		        // Save the new image file
		        String uploadDir = "uploads/cards/" + existingCard.getHotel_id();
		        Path uploadPath = Paths.get(uploadDir);
		        if (!Files.exists(uploadPath)) {
		            Files.createDirectories(uploadPath);
		        }
		        Path fileToCreatePath = uploadPath.resolve(imageName);
		        Files.copy(cardImage.getInputStream(), fileToCreatePath, StandardCopyOption.REPLACE_EXISTING);
		    }

		    // Handle room types
		    List<RoomType> existingRoomTypes = existingCard.getRoomTypes();
		    List<RoomType> updatedRoomTypes = updatedCard.getRoomTypes();

		    // Remove existing room types if needed
		    if (existingRoomTypes.size() > updatedRoomTypes.size()) {
		        for (int i = updatedRoomTypes.size(); i < existingRoomTypes.size(); i++) {
		            existingRoomTypes.remove(i);
		        }
		    }

		    // Update or add new room types
		    for (int i = 0; i < updatedRoomTypes.size(); i++) {
		        RoomType updatedRoomType = updatedRoomTypes.get(i);
		        if (i < existingRoomTypes.size()) {
		            // Update existing room type
		            RoomType existingRoomType = existingRoomTypes.get(i);
		            existingRoomType.setType(updatedRoomType.getType());
		            existingRoomType.setPrice(updatedRoomType.getPrice());
		        } else {
		            // Add new room type
		            existingRoomTypes.add(updatedRoomType);
		            // Automatically set hotel_id for new room types
		            updatedRoomType.setCard(existingCard);
		        }

		        // Update room type image if provided
		        if (roomImages != null && i < roomImages.length && !roomImages[i].isEmpty()) {
		            String roomImageName = roomImages[i].getOriginalFilename();
		            RoomType roomTypeToUpdate = existingRoomTypes.get(i);
		            roomTypeToUpdate.setImageFilename(roomImageName);
		            // Save room type image processing logic here
		            Path path = Paths.get("src/main/resources/static/images/" + roomImages[i].getOriginalFilename());
		            Files.write(path, roomImages[i].getBytes());
		        }
		    }

		    // Save updated card entity
		    existingCard.setRoomTypes(existingRoomTypes);
		    cardRepo.save(existingCard);
		    System.out.println("Card saved successfully");

		    return "redirect:/viewcards";
		}


	
	
	
	@GetMapping("/card/view/{id}")
	public String viewHotel(@PathVariable("id") Integer id, Model model) {
	    Card card = cardRepo.findById(id).orElse(null);
	    if (card != null) {
	        List<ReviewHotel> reviewshotel = card.getReviewshotel();

	        // Calculate average rating
	        double avgRating = reviewshotel.stream()
	                .mapToInt(ReviewHotel::getRating)
	                .average()
	                .orElse(0.0);

	        // Format average rating to one decimal place
	        DecimalFormat df = new DecimalFormat("#.#");
	        String formattedAvgRating = df.format(avgRating);

	        model.addAttribute("card", card);
	        model.addAttribute("reviews", reviewshotel);
	        model.addAttribute("formattedAvgRating", formattedAvgRating);
	        model.addAttribute("averageRating", avgRating); // Add the numeric average rating for comparison
	        model.addAttribute("reviewCount", reviewshotel.size());
	    }
	    return "hotel_details";
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
	return "redirect:/viewrestaurants";
	}
	
	
	@GetMapping("/viewrestaurant")
    public String viewRestaurant(Model model) {
        List<Object[]> results = RestaurantRepo.findAllWithAverageRatings();

        // Convert the results to a list of Restaurants with their ratings
        List<Restaurant> restaurantList = results.stream()
                .map(result -> {
                    Restaurant restaurant = (Restaurant) result[0];
                    Double averageRating = (Double) result[1];
                    restaurant.setAverageRating(averageRating); // Add a setAverageRating method in your Restaurant entity
                    return restaurant;
                })
                .collect(Collectors.toList());

        // Add the sorted restaurantList to the model
        model.addAttribute("restaurantList", restaurantList); 
        return "ViewRestaurantFront"; 
    }
	
	@GetMapping("/restaurant/review/{id}")
	public String getRestaurantById(@PathVariable Integer id, Model model, Principal principal) {
	    Optional<Restaurant> restaurantOptional = restaurantservice.findById(id);
	    if (restaurantOptional.isPresent()) {
	        Restaurant restaurant = restaurantOptional.get();
	        List<ReviewRestaurant> reviewsrestaurant = restaurant.getReviewsrestaurant(); // Fetch package reviews
	        
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
	        
	        model.addAttribute("restaurant", restaurant);
	        model.addAttribute("reviewsrestaurant", reviewsrestaurant); // Add reviews to the model
	        model.addAttribute("loggedInMemberId", loggedInMemberId);
	    }
	    return "Restaurant_details";
	}
	 
	
	

	@PostMapping("/restaurant/{id}/reviews")
	public String addReviewrestaurant(@PathVariable Integer id, 
	                                  @RequestParam String comment, 
	                                  @RequestParam int rating, 
	                                  Principal principal) {
	    Optional<Restaurant> restaurantOptional = restaurantservice.findById(id);
	    if (restaurantOptional.isPresent() && principal != null) {
	        String username = principal.getName();
	        Member member = memberService.findByUsername(username); // Fetch the logged-in member
	        if (member != null) {
	            Restaurant restaurant = restaurantOptional.get(); // Rename to avoid conflict
	            ReviewRestaurant reviewRestaurant = new ReviewRestaurant(comment, rating);
	            reviewRestaurant.setMember(member); // Associate the comment with the logged-in member
	            reviewRestaurant.setRestaurant(restaurant);
	            restaurant.getReviewsrestaurant().add(reviewRestaurant); // Add the review to the restaurant's list of reviews
	            restaurantservice.save(restaurant); // Save the restaurant entity to persist the review
	        }
	    }
	    return "redirect:/restaurant/view/" + id;
	}

	
	
	 @GetMapping("/restaurant/view/{id}")
		public String viewRestaurant(@PathVariable("id") Integer id, Model model) {
		    Restaurant restaurant = RestaurantRepo.findById(id).orElse(null);
		    if (restaurant != null) {
		        List<ReviewRestaurant> reviewsrestaurant = restaurant.getReviewsrestaurant();

		        // Calculate average rating
		        double avgRating = reviewsrestaurant.stream()
		                .mapToInt(ReviewRestaurant::getRating)
		                .average()
		                .orElse(0.0);

		        // Format average rating to one decimal place
		        DecimalFormat df = new DecimalFormat("#.#");
		        String formattedAvgRating = df.format(avgRating);

		        model.addAttribute("restaurant", restaurant);
		        model.addAttribute("reviews", reviewsrestaurant);
		        model.addAttribute("formattedAvgRating", formattedAvgRating);
		        model.addAttribute("averageRating", avgRating); // Add the numeric average rating for comparison
		        model.addAttribute("reviewCount", reviewsrestaurant.size());
		    }
		    return "Restaurant_details";
		}
	 
	 

		@GetMapping("/viewrestaurants")
	    public String viewRestaurants(Model model) {
	        List<Restaurant> restaurantList = restaurantservice.getAllRestaurants(); // Assuming you fetch all cards
	        
	        model.addAttribute("restaurantList", restaurantList);
	        return "ViewRestaurant"; // Assuming your Thymeleaf template name is viewCards.html
	    }
	 
	 
		@GetMapping("/restaurant/edit/{id}")
		public String editRestaurant(@PathVariable("id") Integer id, Model model) {
		    Restaurant restaurant = RestaurantRepo.getReferenceById(id);
		    model.addAttribute("restaurant", restaurant);
		    return "edit_restaurant";
		}
		
		
		@PostMapping("/restaurant/edit/{id}")
		public String updateRestaurant(@PathVariable("id") Integer id, 
		                               @ModelAttribute("restaurant") Restaurant restaurant,
		                               @RequestParam("restaurantImage") MultipartFile file) {
		    Optional<Restaurant> existingRestaurantOpt = RestaurantRepo.findById(id);
		    if (!existingRestaurantOpt.isPresent()) {
		        return "redirect:/viewrestaurants";
		    }
		    Restaurant existingRestaurant = existingRestaurantOpt.get();

		    if (!file.isEmpty()) {
		        try {
		            String uploadDir = "uploads/restaurants/" + existingRestaurant.getRestaurant_id() + "/";
		            Path uploadPath = Paths.get(uploadDir);

		            if (!Files.exists(uploadPath)) {
		                Files.createDirectories(uploadPath);
		            }

		            String fileName = file.getOriginalFilename();
		            Path filePath = uploadPath.resolve(fileName);
		            Files.write(filePath, file.getBytes());

		         // Set the image path relative to the directory
		            existingRestaurant.setImage(fileName); 

		        } catch (IOException e) {
		            e.printStackTrace();
		            // Handle file saving error
		        }
		    }

		    existingRestaurant.setTitle(restaurant.getTitle());
		    existingRestaurant.setDescription(restaurant.getDescription());
		    // Set other fields as needed

		    RestaurantRepo.save(existingRestaurant);

		    return "redirect:/viewrestaurants";
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
	return "redirect:/viewpackages";
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
	public String addReviewpackage(@PathVariable Integer id, 
	                               @RequestParam String comment, 
	                               @RequestParam int rating, 
	                               Principal principal) {
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
	    return "redirect:/package/view/" + id;
	}

	@GetMapping("/package/view/{id}")
	public String viewsingle(@PathVariable("id") Integer id,
	                         @RequestParam(name = "participants", defaultValue = "1") int participants,
	                         Model model) {
	    Optional<Package> packageOpt = packageRepo.findById(id);
	    if (packageOpt.isPresent()) {
	        Package packageObj = packageOpt.get();
	        model.addAttribute("packageObj", packageObj);
	        model.addAttribute("participants", participants);

	        List<ReviewPackage> reviewspackage = packageObj.getReviewspackage();

	        // Calculate average rating
	        double avgRating = packageObj.getReviewspackage().stream()
	                .mapToInt(ReviewPackage::getRating)
	                .average()
	                .orElse(0.0);

	        // Format average rating to one decimal place
	        DecimalFormat df = new DecimalFormat("#.#");
	        String formattedAvgRating = df.format(avgRating);
	        model.addAttribute("formattedAvgRating", formattedAvgRating);

	        int reviewCount = reviewspackage.size();
	        double totalPrice = participants * packageObj.getPrice();

	        model.addAttribute("averageRating", avgRating);
	        model.addAttribute("reviewCount", reviewCount);
	        model.addAttribute("reviewspackage", reviewspackage);
	        model.addAttribute("totalPrice", totalPrice);

	        return "PackageDetails";
	    } else {
	        return "redirect:/error";
	    }
	}
	
	
	@GetMapping("/PackagesView")
    public String viewPackages(Model model) {
        // Fetch Packages with their average ratings
		List<Object[]> result = packageRepo.findAllWithAverageRatings();

        // Convert the results to a list of Package entities with their ratings
        List<Package> packages = result.stream()
                .map(obj -> {
                    Package pkg = (Package) obj[0];
                    Double averageRating = (Double) obj[1];
                    pkg.setAverageRating(averageRating); // Assuming Package entity has a setAverageRating method
                    return pkg;
                })
                .collect(Collectors.toList());

        model.addAttribute("packages", packages);
        return "ViewPackageFront"; // The name of the Thymeleaf template
    }
	

	@GetMapping("/viewpackages")
    public String viewPackage(Model model) {
        List<Package> packageList = packageService.getAllPackages(); // Assuming you fetch all cards
        
        model.addAttribute("packageList", packageList);
        return "ViewPackage"; // Assuming your Thymeleaf template name is viewCards.html
    }
	
	
	@GetMapping("package/edit/{id}")
	public String editPackageForm(@PathVariable("id") Integer id, Model model) {
	    Optional<Package> packageOpt = packageRepo.findById(id);
	    if (packageOpt.isPresent()) {
	        model.addAttribute("package", packageOpt.get());
	        return "edit_package";
	    } else {
	        return "redirect:/viewpackages"; // or handle the error appropriately
	    }
	}

	@PostMapping("/package/edit/{id}")
    public String updatePackage(@PathVariable("id") Integer id, 
                                @ModelAttribute("package") Package packageObj,
                                @RequestParam("packageImage") MultipartFile file) {
        Optional<Package> existingPackageOpt = packageRepo.findById(id);
        if (!existingPackageOpt.isPresent()) {
            return "redirect:/viewpackages"; // Handle the error appropriately
        }
        Package existingPackage = existingPackageOpt.get();

        if (!file.isEmpty()) {
            try {
                String uploadDir = "uploads/packages/" + existingPackage.getPackage_id() + "/";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());

                // Set the image path relative to the directory
                existingPackage.setImage(fileName);

            } catch (IOException e) {
                e.printStackTrace();
                // Handle file saving error
            }
        }

        existingPackage.setTitle(packageObj.getTitle());
        existingPackage.setDescription(packageObj.getDescription());
        existingPackage.setLocation(packageObj.getLocation());
        existingPackage.setPrice(packageObj.getPrice());
        // Set other fields as needed

        packageRepo.save(existingPackage);

        return "redirect:/viewpackages";
	}
}
	
	
	






    
	
	
	
    

