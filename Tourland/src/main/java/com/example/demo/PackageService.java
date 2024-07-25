package com.example.demo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackageService {

    @Autowired
    private PackageRepository packageRepository;
    

    @Autowired
    private TripPackageRepository tripPackageRepository;

    @Autowired
    private CardRepository hotelRepository;

    @Autowired
    private RestaurantRespository restaurantRepository;

    




    public List<Package> findAll() {
        return packageRepository.findAll();
    }

    public Optional<Package> findById(Integer id) {
        return packageRepository.findById(id);
    }

    public Package save(Package packageObj) {
        return packageRepository.save(packageObj);
    }

    public void deleteById(Integer id) {
        packageRepository.deleteById(id);
    }
    
    public List<Package> findPackagesByLocation(String location) {
        return packageRepository.findByLocationContainingIgnoreCase(location);
    }
    
  
    public List<TripPackage> findTripPackagesByLocation(String location) {
        return tripPackageRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Card> findHotelsByLocation(String location) {
        return hotelRepository.findByLocationContainingIgnoreCase(location);
    }

    public List<Restaurant> findRestaurantsByLocation(String location) {
        return restaurantRepository.findByLocationContainingIgnoreCase(location);
    }
    
    public List<Package> getAllPackages() {
        return packageRepository.findAll(); // Implement according to your repository method
    }
    
    

}
