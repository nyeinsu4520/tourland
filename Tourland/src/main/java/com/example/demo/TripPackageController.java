package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin/trip-packages")
public class TripPackageController {

    @Autowired
    private CardRepository hotelRepository;

    @Autowired
    private RestaurantRespository restaurantRepository;

    @Autowired
    private TripPackageRepository tripPackageRepository;
    
    @Autowired
    private TripPackageService tripPackageService;

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("tripPackage", new TripPackage());
        model.addAttribute("allHotels", hotelRepository.findAll());
        model.addAttribute("allRestaurants", restaurantRepository.findAll());
        return "TripPackage";
    }

    @PostMapping("/create")
    public String createTripPackage(@Valid TripPackage TrippackageObj,@ModelAttribute TripPackage tripPackage,@RequestParam("TrippackageImage") MultipartFile imgFile) {
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
        
        return "redirect:/admin/trip-packages/list";
    }
    
    
    @GetMapping("/list")
    public String showTripPackageList(Model model) {
        List<TripPackage> tripPackages = tripPackageRepository.findAll();
        model.addAttribute("tripPackages", tripPackages);
        return "TripPackageList";
    }
    
    
    @GetMapping("/book")
    public String showBookingForm(Model model) {
        List<TripPackage> tripPackages = tripPackageRepository.findAll();
        model.addAttribute("tripPackages", tripPackages);
        model.addAttribute("selectedTripPackage", new TripPackage()); 
        return "TripBooking";
    }

    @GetMapping("/{tripPackageId}/hotels")
    public String getHotelsForTripPackage(@PathVariable Long tripPackageId, Model model) {
        List<Card> hotels = tripPackageService.getHotelsByTripPackage(tripPackageId);
        model.addAttribute("hotels", hotels);
        /*return "Fragment/HotelSelect :: hotelSelectFragment";*/
        return "redirect: /book";
    }

    @GetMapping("/{tripPackageId}/restaurants")
    public String getRestaurantsForTripPackage(@PathVariable Long tripPackageId, Model model) {
        List<Restaurant> restaurants = tripPackageService.getRestaurantsByTripPackage(tripPackageId);
        model.addAttribute("restaurants", restaurants);
        /*return "Fragment/RestaurantSelect :: restaurantSelectFragment";*/
        return "redirect: /book";
    }
    
    @PostMapping("/book")
    public String bookTripPackage(@RequestParam("tripPackageId") Long tripPackageId, 
                                  @RequestParam("hotelId") Integer hotelId, 
                                  @RequestParam("restaurantId") Integer restaurantId) {
       
        return "redirect:/trip-packages/book";
    }
}
