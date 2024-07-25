package com.example.demo;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface CardRepository extends JpaRepository<Card, Integer> {
	
	List<Card> findAllByTripPackageId(Long tripPackageId);
	 List<Card> findByLocationContainingIgnoreCase(String location);
	 
	 @Query("SELECT c FROM Card c LEFT JOIN c.reviewshotel r GROUP BY c ORDER BY AVG(r.rating) DESC")
	    List<Card> findAllOrderByRatingDesc();
	 

	 @Query("SELECT c, COALESCE(AVG(r.rating), 0) AS averageRating " +
	           "FROM Card c LEFT JOIN c.reviewshotel r " +
	           "GROUP BY c.hotel_id")
	    List<Object[]> findAllWithAverageRatings();
	 
	 
	 

}