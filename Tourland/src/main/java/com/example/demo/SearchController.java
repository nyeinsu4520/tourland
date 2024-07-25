package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SearchController {

    private final PackageService packageService;

    public SearchController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("/search")
    public String search(@RequestParam("query") String query, Model model) {
        List<Package> packageResults = packageService.findPackagesByLocation(query);
        List<TripPackage> tripPackageResults = packageService.findTripPackagesByLocation(query);
        List<Card> hotelResults = packageService.findHotelsByLocation(query);
        List<Restaurant> restaurantResults = packageService.findRestaurantsByLocation(query);
        
        model.addAttribute("query", query);
        model.addAttribute("packageResults", packageResults);
        model.addAttribute("tripPackageResults", tripPackageResults);
        model.addAttribute("hotelResults", hotelResults);
        model.addAttribute("restaurantResults", restaurantResults);

        return "search-results";
    }
}
