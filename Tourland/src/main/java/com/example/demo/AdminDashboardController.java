package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class AdminDashboardController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/admin/bookings/{bookingId}/confirm")
    public String confirmBooking(@PathVariable int bookingId) {
        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            Booking booking = optionalBooking.get();
            booking.setStatus(Booking.Status.CONFIRMED);
            bookingRepository.save(booking);

            
        }
        return "redirect:/admin/bookings";
    }

    @PostMapping("/admin/bookings/{bookingId}/reject")
    public String rejectBooking(@PathVariable int bookingId) {
        Booking booking = bookingRepository.getReferenceById(bookingId);
       
            
            booking.setStatus(Booking.Status.REJECTED);
            bookingRepository.save(booking);

            // Notify user about rejection
            String recipient = booking.getEmail();
            String message = "Your booking with ID " + booking.getBooking_id() + " has been rejected.";
            Notification notification = new Notification(message, recipient);
            notificationRepository.save(notification);
        
        return "redirect:/admin/bookings";
    }

    @GetMapping("/admin/bookings")
    public String adminDashboard(Model model) {
        List<Booking> bookings = bookingRepository.findAll();
        model.addAttribute("bookings", bookings);
        return "adminDashboard";
    }
}
