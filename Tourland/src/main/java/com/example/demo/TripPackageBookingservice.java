package com.example.demo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.TripPackageBooking.Status;

public class TripPackageBookingservice {
	
    @Autowired
    private TripPackageBookingRepository tripPackageBookingRepository;

    public TripPackageBooking createBooking(TripPackageBooking booking) {
        booking.setStatus(TripPackageBooking.Status.PENDING);
        return tripPackageBookingRepository.save(booking);
    }

    public void save(TripPackageBooking booking) {
        tripPackageBookingRepository.save(booking);
    }
    
    


}
