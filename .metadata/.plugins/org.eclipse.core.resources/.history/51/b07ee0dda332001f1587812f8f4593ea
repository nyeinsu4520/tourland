package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class bookingservice {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking createBooking(Booking booking) {
        booking.setStatus(Booking.Status.PENDING);
        return bookingRepository.save(booking);
    }
    
    public void save(Booking booking) {
        bookingRepository.save(booking);
    }
}