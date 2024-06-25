package com.example.demo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NotificationController {

    @Autowired
    private BookingRepository bookingRepository; // Autowire BookingRepository

    @GetMapping("/notify/{bookingId}")
    public String notifyUser(@PathVariable int bookingId, Model model) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId); // Use autowired repository instance
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            model.addAttribute("booking", booking);
            return "notipage"; // Assuming notipage.html is your notification template
        }
        return "redirect:/admin/bookings"; // Redirect to admin dashboard if booking not found
    }
}
