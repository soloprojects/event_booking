package com.event.booking.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final EventNotification eventNotification;

    //@Scheduled(cron = "0 0 0 * * ?") // Run every day at midnight
    @Scheduled(cron = "0 * * * * *")   //Run every minute
    public void checkUpcomingEvents() {
        eventNotification.sendNotificationsForUpcomingEvents();
    }
}
