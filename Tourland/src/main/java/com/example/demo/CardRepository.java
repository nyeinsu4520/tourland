package com.example.demo;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface CardRepository extends JpaRepository<Card, Integer> {
	
	List<Card> findAllByTripPackageId(Long tripPackageId);
}