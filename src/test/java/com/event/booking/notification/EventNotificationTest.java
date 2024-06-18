package com.event.booking.notification;

import com.event.booking.entity.Event;
import com.event.booking.entity.EventCategory;
import com.event.booking.entity.User;
import com.event.booking.repository.EventRepository;
import com.event.booking.repository.UserRepository;
import nl.altindag.log.LogCaptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EventNotificationTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private EventNotification eventNotification;

    @Test
    void testSendNotificationsForUpcomingEvents() {
        LogCaptor logCaptor = LogCaptor.forClass(EventNotification.class);

        // Given
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        Event event1 = new Event();
        event1.setId(1L);
        event1.setName("Test Event");
        event1.setEventDescription("Description");
        event1.setCategory(EventCategory.GAME);
        event1.setDate(tomorrow);
        event1.setAvailableAttendeesCount(100);

        Event event2 = new Event();
        event2.setId(2L);
        event2.setName("New Event");
        event2.setEventDescription("More Description");
        event2.setCategory(EventCategory.CONFERENCE);
        event2.setDate(tomorrow);
        event2.setAvailableAttendeesCount(200);

        List<Event> upcomingEvents = Arrays.asList(event1,event2);

        List<User> users = new ArrayList<>();
        users.add(User.builder().id(1L).email("user1@example.com").build());
        users.add(User.builder().id(2L).email("user2@example.com").build());

        given(eventRepository.findEventsByDate(tomorrow)).willReturn(upcomingEvents);
        given(userRepository.findAll()).willReturn(users);

        // When
        eventNotification.sendNotificationsForUpcomingEvents();

        // Then
        then(eventRepository).should().findEventsByDate(tomorrow);
        then(userRepository).should().findAll();
        then(emailService).should().send(users, upcomingEvents.get(0));
        then(emailService).should().send(users, upcomingEvents.get(1));

        // Assert
        verify(emailService, times(2)).send(eq(users), any(Event.class));
        assertThat(logCaptor.getLogs()).hasSize(2);
    }
}
