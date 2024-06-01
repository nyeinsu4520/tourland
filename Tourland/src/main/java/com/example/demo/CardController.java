package com.example.demo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	

	@GetMapping("/index")
    public String getIndex(Model model) {
        List<Card> cardlist = cardRepo.findAll();
        List<Restaurant> restaurantlist = RestaurantRepo.findAll();
        List<Package> packagelist=packageRepo.findAll();
        model.addAttribute("cardlist", cardlist);
        model.addAttribute("restaurantlist", restaurantlist);
        model.addAttribute("packagelist",packagelist);
        return "index";
    }
	    


	
	@GetMapping("/cards/add")
	public String addItem(Model model) {
		model.addAttribute("card",new Card());
		List<Card> cardlist=cardRepo.findAll();
		model.addAttribute("cardlist",cardlist);
		return "add_card";
	}
	
	
	@PostMapping("/cards/save")
	public String saveItem(@Valid Card card,BindingResult bindingResult, @RequestParam("cardImage") MultipartFile imgFile,Model model) {
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
	String uploadDir = "uploads/cards/" + savedItem.getId();
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
	String uploadDir = "uploads/restaurants/" + savedItem.getId();
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
	String uploadDir = "uploads/packages/" + savedItem.getId();
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
	
	

}

    
	
	
	
    

