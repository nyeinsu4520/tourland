package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRestaurantRepository extends JpaRepository<ReviewRestaurant, Integer> {
    
	 List<ReviewRestaurant> findByRestaurant(Restaurant restaurant);
	    

}