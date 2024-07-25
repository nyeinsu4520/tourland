package com.example.demo;

import com.example.demo.TripPackageBooking.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class AdminDashboardController {

    private static final Logger logger = Logger.getLogger(AdminDashboardController.class.getName());

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TripPackageBookingRepository tripPackageBookingRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MemberRepository memberRepository;
    
    

    @PostMapping("/admin/bookings/{bookingId}/confirm")
    public String confirmBooking(@PathVariable int bookingId, Principal principal) {
        Booking booking = bookingRepository.findById(bookingId)
                                           .orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + bookingId));
        booking.setStatus(Booking.Status.CONFIRMED);
        bookingRepository.save(booking);

        // Retrieve username from Principal
        String username = principal != null ? principal.getName() : "Unknown";

        Member member = memberRepository.findByEmail(booking.getEmail());
        if (member != null) {
            String message = "Your booking with ID " + booking.getBooking_id() + " has been confirmed.";
            Notification notification = new Notification(message, member);
            notificationRepository.save(notification);
            logger.info("Notification saved for member: " + member.getId() + ", Username: " + username);
        } else {
            logger.warning("Member not found for email: " + booking.getEmail() + ", Username: " + username);
            // Handle the scenario where member is not found, maybe return an error page or redirect
        }

        return "redirect:/admin/bookings";
    }

    @PostMapping("/admin/bookings/{bookingId}/reject")
    public String rejectBooking(@PathVariable int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                                           .orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + bookingId));
        booking.setStatus(Booking.Status.REJECTED);
        bookingRepository.save(booking);

        Member member = memberRepository.findByEmail(booking.getEmail());
        if (member != null) {
            String message = "Your booking with ID " + booking.getBooking_id() + " has been rejected.";
            Notification notification = new Notification(message, member);
            notificationRepository.save(notification);
            logger.info("Notification saved for member: " + member.getId());
        } else {
            logger.warning("Member not found for email: " + booking.getEmail());
        }

        return "redirect:/admin/bookings";
    }

    @PostMapping("/admin/trip-package-bookings/{tripPackagebookingId}/confirm")
    public String confirmTripPackageBooking(@PathVariable Long tripPackagebookingId, Principal principal) {
        TripPackageBooking tripPackageBooking = tripPackageBookingRepository.findById(tripPackagebookingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trip package booking Id:" + tripPackagebookingId));
        tripPackageBooking.setStatus(TripPackageBooking.Status.CONFIRMED); // Use the appropriate enum
        tripPackageBookingRepository.save(tripPackageBooking);

        // Retrieve username from Principal
        String username = principal != null ? principal.getName() : "Unknown";

        Member member = memberRepository.findByEmail(tripPackageBooking.getEmail());
        if (member != null) {
            String message = "Your trip package booking with ID " + tripPackageBooking.getId() + " has been confirmed.";
            Notification notification = new Notification(message, member);
            notificationRepository.save(notification);
            logger.info("Notification saved for member: " + member.getId() + ", Username: " + username);
        } else {
            logger.warning("Member not found for email: " + tripPackageBooking.getEmail() + ", Username: " + username);
            // Handle the scenario where member is not found, maybe return an error page or redirect
        }

        return "redirect:/admin/bookings";
    }

    @PostMapping("/admin/trip-package-bookings/{tripPackagebookingId}/reject")
    public String rejectTripPackageBooking(@PathVariable Long tripPackagebookingId) {
        TripPackageBooking tripPackageBooking = tripPackageBookingRepository.findById(tripPackagebookingId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid trip package booking Id:" + tripPackagebookingId));
        tripPackageBooking.setStatus(TripPackageBooking.Status.REJECTED); // Use the appropriate enum
        tripPackageBookingRepository.save(tripPackageBooking);

        Member member = memberRepository.findByEmail(tripPackageBooking.getEmail());
        if (member != null) {
            String message = "Your trip package booking with ID " + tripPackageBooking.getId() + " has been rejected.";
            Notification notification = new Notification(message, member);
            notificationRepository.save(notification);
            logger.info("Notification saved for member: " + member.getId());
        } else {
            logger.warning("Member not found for email: " + tripPackageBooking.getEmail());
        }

        return "redirect:/admin/bookings";
    }




    @GetMapping("/admin/bookings")
    public String adminDashboard(Model model) {
        List<Booking> bookings = bookingRepository.findAll();
        List<TripPackageBooking> tripPackageBookings = tripPackageBookingRepository.findAll();

        model.addAttribute("bookings", bookings);
        model.addAttribute("tripPackageBookings", tripPackageBookings);
        System.out.println("Booking records"+tripPackageBookings.size());
        
        return "adminDashboard";
    }
}
