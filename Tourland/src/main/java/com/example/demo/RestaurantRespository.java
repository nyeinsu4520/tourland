package com.example.demo;


import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRespository extends JpaRepository<Restaurant, Integer> {
}
