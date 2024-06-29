package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;
import java.security.Principal;
import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private NotificationService notificationService;

    
    private static final Logger logger = Logger.getLogger(NotificationController.class.getName());
    @GetMapping("/notifications")
    public String showNotifications(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Member member = memberService.findByUsername(username);

            if (member != null) {
                List<Notification> notifications = notificationRepository.findByMemberIdOrderByTimestampDesc(member.getId());
                model.addAttribute("notifications", notifications);
            } else {
                // Handle case where member is not found for the logged-in username
                // This might indicate a problem with user authentication or setup
                // You can log a warning or redirect to an error page
                logger.warning("Member not found for username: " + username);
            }
        }

        return "notipage"; // Ensure this template exists
    }
    
    @GetMapping("/notifications/count")
    @ResponseBody
    public long getNotificationCount(Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            Member member = memberService.findByUsername(username);

            if (member != null) {
                return notificationService.getUnreadCountForMember(member.getId());
            }
        }
        return 0;
    }
    
    



}
