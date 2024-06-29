package com.example.demo;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private MemberRepository memberRepository;

    private static final Logger logger = Logger.getLogger(NotificationService.class.getName());

    public void createNotification(String message, Integer memberId) {
        try {
            Member member = memberRepository.findById(memberId).orElse(null);

            if (member != null) {
                Notification notification = new Notification(message, member);
                notificationRepository.save(notification);
                logger.info("Notification saved for member: " + member.getId());
            } else {
                logger.warning("Member not found for memberId: " + memberId);
            }
        } catch (Exception e) {
            logger.severe("Error saving notification: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error saving notification");
        }
    } 
    
    public List<Notification> getNotificationsForMember(Integer memberId) {
        return notificationRepository.findByMemberIdOrderByTimestampDesc(memberId);
    }

    public long getUnreadCountForMember(Integer memberId) {
        return notificationRepository.findByMemberIdOrderByTimestampDesc(memberId).stream()
                .filter(notification -> !notification.isRead())
                .count();
    }
    
    
}
