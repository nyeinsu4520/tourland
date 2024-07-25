package com.example.demo;


import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TripPackageRepository extends JpaRepository<TripPackage, Long>{

	List<TripPackage> findByLocationContainingIgnoreCase(String location);
	
	
	@Query("SELECT r, COALESCE(AVG(rev.rating), 0) AS averageRating " +
	           "FROM TripPackage r LEFT JOIN r.reviewsTripPackage rev " +
	           "GROUP BY r.id")
	    List<Object[]> findAllWithAverageRatings();
	    
	    public Optional<TripPackage> findById(Long id);
	    
	    
	    @Query("SELECT tp FROM TripPackage tp WHERE tp.endDate < :date")
	    List<TripPackage> findExpiredTripPackages(LocalDate date);
	    
	 // This method should exist in JpaRepository
	    void deleteById(Long id); 
	    
	  
	    
	    
}
