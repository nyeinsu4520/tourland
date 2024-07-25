package com.example.demo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TripPackageBookingRepository extends JpaRepository<TripPackageBooking, Long> {
	
	List<TripPackageBooking> findByMember(Member member);
    List<TripPackageBooking> findByTripPackageId(Long tripPackageId);
    
  
}

