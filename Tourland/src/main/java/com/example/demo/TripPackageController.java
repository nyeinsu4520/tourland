package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class TripPackageController {

    @Autowired
    private CardRepository hotelRepository;
    
    @Autowired
    private RestaurantService restaurantservice;

    @Autowired
    private CardService cardservice;
    
    @Autowired 
    private BookingRepository bookingrepo;
    
    @Autowired
    private RestaurantRespository restaurantRepository;

    @Autowired
    private TripPackageRepository tripPackageRepository;
    
    @Autowired
    private TripPackageService tripPackageService;
    
    @Autowired
    private MemberService memberservice;
    
    @Autowired
    private MemberRepository memberrepository;
    
    @Autowired
    private RoomTypeService roomTypeService;
    
    @Autowired
    private TripPackageBookingRepository TripPackageBookingRepository;

    @GetMapping("/trippackages/add")
    public String showCreateForm(Model model) {
        model.addAttribute("tripPackage", new TripPackage());
        model.addAttribute("allHotels", hotelRepository.findAll());
        model.addAttribute("allRestaurants", restaurantRepository.findAll());
        return "TripPackage";
    }

    @PostMapping("/trippackages/add")
    public String saveTripPackage(@Valid TripPackage TrippackageObj,@ModelAttribute TripPackage tripPackage,@RequestParam("TrippackageImage") MultipartFile imgFile) {
        Set<Card> selectedHotels = new HashSet<>();
        Set<Restaurant> selectedRestaurants = new HashSet<>();

        if (tripPackage.getHotelIds() != null) {
            for (Integer hotelId : tripPackage.getHotelIds()) {
                hotelRepository.findById(hotelId).ifPresent(selectedHotels::add);
            }
        }

        if (tripPackage.getRestaurantIds() != null) {
            for (Integer restaurantId : tripPackage.getRestaurantIds()) {
                restaurantRepository.findById(restaurantId).ifPresent(selectedRestaurants::add);
            }
        }

        tripPackage.setHotels(selectedHotels);
        tripPackage.setRestaurants(selectedRestaurants);
        
        String imageName = imgFile.getOriginalFilename();
    	// set the image name in item object
        TrippackageObj.setImage(imageName);
    	// save the item obj to the db
    	TripPackage savedItem = tripPackageRepository.save(TrippackageObj);
    	try {
    	// prepare the directory path
    	String uploadDir = "uploads/trippackages/" + savedItem.getId();
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
    	
        

        tripPackageRepository.save(tripPackage);
        
        return "redirect:/viewtrippackages";
    }
    
    
    

    @GetMapping("/trippackage/view/{id}")
    public String viewTripPackage(@PathVariable Long id, Model model, Principal principal) {
        Optional<TripPackage> tripPackageOptional = tripPackageService.findByTripPackageId(id);
        if (tripPackageOptional.isPresent()) {
            TripPackage tripPackage = tripPackageOptional.get();
            List<ReviewTripPackage> reviewsTripPackage = tripPackage.getReviewsTripPackage(); // Fetch package reviews

            // Calculate average rating
            double avgRating = reviewsTripPackage.stream()
                    .mapToInt(ReviewTripPackage::getRating)
                    .average()
                    .orElse(0.0);

            // Format average rating to one decimal place
            DecimalFormat df = new DecimalFormat("#.#");
            String formattedAvgRating = df.format(avgRating);

            // Get the logged-in member's ID
            String loggedInMemberId = "";
            if (principal != null) {
                // Retrieve the username from the principal
                String username = principal.getName();
                // Use the username to find the member ID
                String memberId = memberservice.findIdByUsername(username);
                if (memberId != null) {
                    loggedInMemberId = memberId;
                }
            }

            model.addAttribute("tripPackage", tripPackage);
            model.addAttribute("reviews", reviewsTripPackage); // Add reviews to the model
            model.addAttribute("formattedAvgRating", formattedAvgRating);
            model.addAttribute("averageRating", avgRating); // Add the numeric average rating for comparison
            model.addAttribute("reviewCount", reviewsTripPackage.size());
            model.addAttribute("loggedInMemberId", loggedInMemberId);
            model.addAttribute("review", new ReviewTripPackage()); // Add an empty review object for the form
        }
        return "TripPackageDetails";
    }

	 
    
    @GetMapping("/trippackage/review/{id}")
    public String getTripPackageById(@PathVariable Long id, Model model, Principal principal) {
        Optional<TripPackage> tripPackageOptional = tripPackageService.findByTripPackageId(id);
        if (tripPackageOptional.isPresent()) {
            TripPackage tripPackage = tripPackageOptional.get();
            List<ReviewTripPackage> reviews = tripPackage.getReviewsTripPackage();
            
            // Get the logged-in member's ID
            String loggedInMemberId = "";
            if (principal != null) {
                String username = principal.getName();
                String memberId = memberservice.findIdByUsername(username); // Replace with your member service method to find ID
                if (memberId != null) {
                    loggedInMemberId = memberId;
                }
            }
            
            model.addAttribute("tripPackage", tripPackage);
            model.addAttribute("reviews", reviews);
            model.addAttribute("loggedInMemberId", loggedInMemberId);
        }
        return "TripPackageDetails"; // Check if this matches your actual Thymeleaf template name
    }

    @PostMapping("/trippackage/{id}/reviews")
    public String addReviewTripPackage(@PathVariable Long id,
                                       @RequestParam String comment, 
                                       @RequestParam int rating, 
                                       Principal principal) {
        Optional<TripPackage> tripPackageOptional = tripPackageService.findByTripPackageId(id);
        if (tripPackageOptional.isPresent() && principal != null) {
            String username = principal.getName();
            Member member = memberservice.findByUsername(username);
            if (member != null) {
                TripPackage tripPackage = tripPackageOptional.get();

                // Create and set the review
                ReviewTripPackage reviewTripPackage = new ReviewTripPackage(comment, rating);
                reviewTripPackage.setMember(member);
                reviewTripPackage.setTripPackage(tripPackage);

                // Add the review to the trip package's list of reviews
                tripPackage.getReviewsTripPackage().add(reviewTripPackage);

                // Save the trip package to persist the review
                tripPackageService.save(tripPackage);
            }
        }
        return "redirect:/trippackage/view/" + id;
    }

   
    
    

    
    @GetMapping("/trippackages/list")
    public String showTripPackageList(Model model) {
        List<TripPackage> tripPackages = tripPackageRepository.findAll();
        model.addAttribute("tripPackages", tripPackages);
        return "TripPackageList";
    }
    
 
    @GetMapping("/TripPackagesView")
    public String viewTripPackages(Model model) {
        // Fetch TripPackages with their average ratings
        List<Object[]> result = tripPackageRepository.findAllWithAverageRatings();

        // Convert the results to a list of TripPackage entities with their ratings
        List<TripPackage> tripPackages = result.stream()
                .map(obj -> {
                    TripPackage tripPackage = (TripPackage) obj[0];
                    Double averageRating = (Double) obj[1];
                    tripPackage.setAverageRating(averageRating); // Assuming TripPackage entity has a setAverageRating method
                    return tripPackage;
                })
                .collect(Collectors.toList());

        model.addAttribute("tripPackages", tripPackages);
        return "TripPackagesView"; // The name of the Thymeleaf template
    }
    
 
    
    
    
    
    
    @PostMapping("/trippackages/book")
    public String bookTripPackage(@RequestParam("tripPackageId") Long tripPackageId,
                                  @RequestParam("hotelId") int hotelId,
                                  @RequestParam("roomtypeId") Long roomtypeId,
                                  @RequestParam("restaurantId") int restaurantId,
                                  @RequestParam("participants") int participants,
                                  @RequestParam("email") String email,
                                  @RequestParam("phone") String phone) {
    	// Fetch the selected trip package, hotel, and room type
        TripPackage tripPackage = tripPackageService.findById(tripPackageId);
        RoomType roomType = roomTypeService.findById(roomtypeId);
    	
        TripPackageBooking booking = new TripPackageBooking();

        Optional<TripPackage> tripPackageOpt = tripPackageRepository.findById(tripPackageId);
        if (tripPackageOpt.isPresent()) {
            booking.setTripPackage(tripPackageOpt.get());
        }

        Optional<Card> hotelOpt = hotelRepository.findById(hotelId);
        if (hotelOpt.isPresent()) {
            booking.setHotel(hotelOpt.get());
        }

        Optional<Restaurant> restaurantOpt = restaurantRepository.findById(restaurantId);
        if (restaurantOpt.isPresent()) {
            booking.setRestaurant(restaurantOpt.get());
        }

        
        
        
        booking.setParticipants(participants);
        booking.setEmail(email);
        booking.setPhone(phone);
        booking.setRoomType(roomType);
        booking.setBookingDate(LocalDate.now());
        booking.setStatus(TripPackageBooking.Status.PENDING); // Set status to PENDING
        
        // Retrieve authenticated member
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            Member member = memberrepository.findByUsername(username);
            if (member != null) {
                booking.setMember(member); // Set the authenticated member
            }
        }


        TripPackageBookingRepository.save(booking);

        return "redirect:/trippackages/bookings";
    }

    @GetMapping("/trippackages/bookings")
    public String Bookingsconfirmation(Model model) {
        // Retrieve currently authenticated member
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberservice.findByUsername(username); // Implement this method in your service

        // Retrieve bookings for the authenticated member
        List<TripPackageBooking> bookings = TripPackageBookingRepository.findByMember(member);

        // Fetch room types for all bookings and add them to the model
        Map<Long, String> roomTypeMap = new HashMap<>();
        for (TripPackageBooking booking : bookings) {
            RoomType roomType = booking.getRoomType();
            String roomTypeName = roomType != null ? roomType.getType() : "N/A";
            roomTypeMap.put(booking.getId(), roomTypeName);
        }
        
        

        model.addAttribute("bookings", bookings);
        model.addAttribute("roomTypeMap", roomTypeMap); // Add room type data to the model
        
       
        return "bookingConfirmation"; // Ensure this view name matches your Thymeleaf template
    }
    
    
    @GetMapping("/bookings")
    public String showBookings(Model model) {
        // Retrieve currently authenticated member
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Member member = memberservice.findByUsername(username); // Implement this method in your service

        // Retrieve bookings for the authenticated member
        List<TripPackageBooking> bookings = TripPackageBookingRepository.findByMember(member);

        // Fetch room types for all bookings and add them to the model
        Map<Long, String> roomTypeMap = new HashMap<>();
        for (TripPackageBooking booking : bookings) {
            RoomType roomType = booking.getRoomType();
            String roomTypeName = roomType != null ? roomType.getType() : "N/A";
            roomTypeMap.put(booking.getId(), roomTypeName);
        }
        
        

        model.addAttribute("bookings", bookings);
        model.addAttribute("roomTypeMap", roomTypeMap); // Add room type data to the model
        
        List<Booking> packageBookings = bookingrepo.findByMember(member); // Assuming you fetch all cards
        
        model.addAttribute("packageBookings", packageBookings);
        return "TripBooking"; // Ensure this view name matches your Thymeleaf template
    }

    
    
    @GetMapping("/viewtrippackages")
    public String viewTripPackage(Model model) {
        List<TripPackage> trippackageList = tripPackageService.getAllTripPackages(); // Assuming you fetch all cards
        
        model.addAttribute("trippackageList", trippackageList);
        return "ViewTripPackage"; // Assuming your Thymeleaf template name is viewCards.html
    }
	
    
    
    @GetMapping("/tripPackage/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<TripPackage> tripPackageOpt = tripPackageService.getTripPackageById(id);
        if (!tripPackageOpt.isPresent()) {
            return "redirect:/ViewTripPackage";
        }

        TripPackage tripPackage = tripPackageOpt.get();
        model.addAttribute("tripPackage", tripPackage);
        
        List<Card> allHotels = cardservice.getAllHotels();
        List<Restaurant> allRestaurants = restaurantservice.getAllRestaurants();
        
        model.addAttribute("allHotels", allHotels);
        model.addAttribute("allRestaurants", allRestaurants);

        return "edit_trippackage";
    }

    
    @PostMapping("/tripPackage/edit/{id}")
    public String updateTripPackage(@PathVariable("id") Long id,
                                    @Valid @ModelAttribute TripPackage tripPackageObj,
                                    BindingResult result,
                                    @RequestParam("trippackageImage") MultipartFile file,
                                    Model model) {
        if (result.hasErrors()) {
            // Log validation errors
            result.getAllErrors().forEach(error -> System.out.println(error.toString()));

            // Re-fetch the trip package and other necessary data to return to the form with errors
            Optional<TripPackage> existingTripPackageOpt = tripPackageService.getTripPackageById(id);
            if (!existingTripPackageOpt.isPresent()) {
                return "redirect:/viewtrippackages";
            }

            TripPackage existingTripPackage = existingTripPackageOpt.get();
            model.addAttribute("tripPackage", existingTripPackage);

            List<Card> allHotels = cardservice.getAllHotels();
            List<Restaurant> allRestaurants = restaurantservice.getAllRestaurants();
            model.addAttribute("allHotels", allHotels);
            model.addAttribute("allRestaurants", allRestaurants);

            return "edit_trippackage";
        }

        Optional<TripPackage> existingTripPackageOpt = tripPackageService.getTripPackageById(id);
        if (!existingTripPackageOpt.isPresent()) {
            return "redirect:/viewtrippackages";
        }

        TripPackage existingTripPackage = existingTripPackageOpt.get();

        // Handle file upload
        if (!file.isEmpty()) {
            try {
                String uploadDir = "uploads/trippackages/" + existingTripPackage.getId() + "/";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String fileName = file.getOriginalFilename();
                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());

                existingTripPackage.setImage(fileName); // Set the relative path or filename

            } catch (IOException e) {
                e.printStackTrace();
                // Handle file saving error (consider adding user feedback)
                model.addAttribute("fileUploadError", "File upload failed. Please try again.");
                return "edit_trippackage";
            }
        }

        // Update other fields
        existingTripPackage.setName(tripPackageObj.getName());
        existingTripPackage.setDescription(tripPackageObj.getDescription());
        existingTripPackage.setLocation(tripPackageObj.getLocation());
        existingTripPackage.setPrice(tripPackageObj.getPrice());
        existingTripPackage.setStartDate(tripPackageObj.getStartDate());
        existingTripPackage.setEndDate(tripPackageObj.getEndDate());

        // Clear existing associations and set the new ones
        existingTripPackage.getHotels().clear();
        existingTripPackage.getHotels().addAll(tripPackageObj.getHotels());

        existingTripPackage.getRestaurants().clear();
        existingTripPackage.getRestaurants().addAll(tripPackageObj.getRestaurants());

        tripPackageService.saveTripPackage(existingTripPackage);

        return "redirect:/viewtrippackages";
    }
    
    
    @PostMapping("/tripPackage/delete/{id}")
    public String deleteTripPackage(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            tripPackageService.deleteTripPackage(id);
            redirectAttributes.addFlashAttribute("success", "Trip Package deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete Trip Package.");
            e.printStackTrace(); // Optional: Print stack trace for debugging
        }
        return "redirect:/viewtrippackages"; // Redirect to the view where trip packages are listed
    }
    
}
