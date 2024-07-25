package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import com.example.demo.TripPackageBooking.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TripPackageService {

    @Autowired
    private TripPackageRepository tripPackageRepository;
    
    @Autowired
    private TripPackageBookingRepository trippackagebookingrepo;

    @Autowired
    private CardRepository hotelRepository;

    @Autowired
    private RestaurantRespository restaurantRepository;

    public List<TripPackage> getAllTripPackages() {
        return tripPackageRepository.findAll();
    }
    
    public TripPackage findById(Long id) {
        return tripPackageRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Trip Package not found with id " + id));
    }
    
    @Transactional(readOnly = true)
    public List<Card> getHotelsByTripPackage(Long tripPackageId) {
        return hotelRepository.findAllByTripPackageId(tripPackageId);
    }

    @Transactional(readOnly = true)
    public List<Restaurant> getRestaurantsByTripPackage(Long tripPackageId) {
        return restaurantRepository.findAllByTripPackageId(tripPackageId);
    }
    
    public Optional<TripPackage> findByTripPackageId(Long id) {
        return tripPackageRepository.findById(id);
    }
    

    public void save(TripPackage tripPackage) {
        tripPackageRepository.save(tripPackage);
    }
    
    public Optional<TripPackage> getTripPackageById(Long id) {
        return tripPackageRepository.findById(id);
    }

    public void saveTripPackage(TripPackage tripPackage) {
        tripPackageRepository.save(tripPackage);
    }
    
    public List<TripPackage> findExpiredTripPackages(LocalDate date) {
        return tripPackageRepository.findExpiredTripPackages(date);
    }

    public void deleteTripPackageById(Long id) {
        tripPackageRepository.deleteById(id);
    }
    
    @Transactional
    public void deleteTripPackage(Long id) {
        // Handle related bookings
        List<TripPackageBooking> bookings = trippackagebookingrepo.findByTripPackageId(id);
        for (TripPackageBooking booking : bookings) {
            // Update booking status or disassociate it
            booking.setStatus(TripPackageBooking.Status.CONFIRMED); // Example status update
            trippackagebookingrepo.save(booking);
        }

        // Now delete the trip package
        tripPackageRepository.deleteById(id);
    }

    private void handleOrphanedBookings(Long tripPackageId) {
        // Find all bookings associated with the deleted trip package
        List<TripPackageBooking> bookings = trippackagebookingrepo.findByTripPackageId(tripPackageId);

        for (TripPackageBooking booking : bookings) {
            // Update booking status or handle orphaned bookings
            booking.setStatus(Status.CONFIRMED); // Update to a status indicating cancellation or removal
            trippackagebookingrepo.save(booking);

            // Optionally, notify users about the cancellation
            // sendCancellationNotification(booking.getUser(), booking);
        }
    }
    
}
