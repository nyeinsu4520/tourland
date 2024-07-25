package com.example.demo;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface RestaurantRespository extends JpaRepository<Restaurant, Integer> {
	
	 List<Restaurant> findAllByTripPackageId(Long tripPackageId);
	 
	 List<Restaurant> findByLocationContainingIgnoreCase(String location);

	 
	 @Query("SELECT r, COALESCE(AVG(rev.rating), 0) AS averageRating " +
	           "FROM Restaurant r LEFT JOIN r.reviewsrestaurant rev " +
	           "GROUP BY r.restaurant_id")
	    List<Object[]> findAllWithAverageRatings();
}
