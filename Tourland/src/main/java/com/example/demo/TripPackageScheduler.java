package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class TripPackageScheduler {

    @Autowired
    private TripPackageService tripPackageService;
    
    @Autowired
    private TripPackageRepository tripPackageRepo;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void removeExpiredTripPackages() {
        LocalDate today = LocalDate.now();
        List<TripPackage> expiredPackages = tripPackageRepo.findExpiredTripPackages(today);

        for (TripPackage expiredPackage : expiredPackages) {
        	tripPackageRepo.deleteById(expiredPackage.getId());
        }
    }
}
