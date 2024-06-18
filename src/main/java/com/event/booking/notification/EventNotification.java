package com.event.booking.notification;

import com.event.booking.entity.Event;
import com.event.booking.entity.User;
import com.event.booking.repository.EventRepository;
import com.event.booking.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventNotification {

    private final EventRepository eventRepository;
    private final EmailService emailService;

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(EventNotification.class);

    public void sendNotificationsForUpcomingEvents() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Event> upcomingEvents = eventRepository.findEventsByDate(tomorrow);
        List<User> users = userRepository.findAll();

        for (Event event : upcomingEvents) {
            //Send email to users
            emailService.send(users, event);
            // Log to file for audit
            logAudit(event);
        }
    }

    public void logAudit(Event event) {
        logger.info("Audit log: Notification sent for event - ID: {}, Name: {}, Date: {}", event.getId(), event.getName(), event.getDate());
    }
}
