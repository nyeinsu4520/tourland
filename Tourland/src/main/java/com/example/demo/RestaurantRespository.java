package com.example.demo;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface RestaurantRespository extends JpaRepository<Restaurant, Integer> {
	
	 List<Restaurant> findAllByTripPackageId(Long tripPackageId);
}
