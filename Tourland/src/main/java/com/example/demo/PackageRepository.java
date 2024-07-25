package com.example.demo;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PackageRepository extends JpaRepository<Package, Integer> {
	
	List<Package> findByLocationContainingIgnoreCase(String location);
	
	
	@Query("SELECT p, COALESCE(AVG(rp.rating), 0) AS averageRating " +
	           "FROM Package p LEFT JOIN p.reviewspackage rp " +
	           "GROUP BY p")
	    List<Object[]> findAllWithAverageRatings();
	    
	    
}
