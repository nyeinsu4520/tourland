package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

	private final RestaurantRespository restaurantRepository;
    private final ReviewRestaurantRepository reviewRestaurantRepository;

    public RestaurantService(RestaurantRespository restaurantRepository, ReviewRestaurantRepository reviewRestaurantRepository) {
        this.restaurantRepository = restaurantRepository;
        this.reviewRestaurantRepository = reviewRestaurantRepository;
    }

    public Optional<Restaurant> findById(Integer id) {
        return restaurantRepository.findById(id);
    }

    public List<ReviewRestaurant> getRestaurantReviews(Restaurant restaurant) {
        return reviewRestaurantRepository.findByRestaurant(restaurant);
    }
    
    

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    

    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public void deleteById(Integer id) {
        restaurantRepository.deleteById(id);
    }
    
    
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll(); // Implement according to your repository method
    }

  
   
}

