package com.example.demo;

import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {

    public void subscribe(String email) {
        // Logic to handle subscription, e.g., save to database or send an email
        // For example, save the email to a database
        // subscriptionRepository.save(new Subscription(email));
    }
}