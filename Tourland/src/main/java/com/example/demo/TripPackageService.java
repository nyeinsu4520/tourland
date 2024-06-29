package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TripPackageService {

    @Autowired
    private TripPackageRepository tripPackageRepository;

    @Autowired
    private CardRepository hotelRepository;

    @Autowired
    private RestaurantRespository restaurantRepository;

    public List<TripPackage> getAllTripPackages() {
        return tripPackageRepository.findAll();
    }
    
    
    @Transactional(readOnly = true)
    public List<Card> getHotelsByTripPackage(Long tripPackageId) {
        return hotelRepository.findAllByTripPackageId(tripPackageId);
    }

    @Transactional(readOnly = true)
    public List<Restaurant> getRestaurantsByTripPackage(Long tripPackageId) {
        return restaurantRepository.findAllByTripPackageId(tripPackageId);
    }
}
