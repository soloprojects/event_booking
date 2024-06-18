package com.event.booking.services;

import com.event.booking.dto.EventRequest;
import com.event.booking.dto.ReserveRequest;
import com.event.booking.entity.Event;
import com.event.booking.entity.EventCategory;
import com.event.booking.exception.BusinessException;
import com.event.booking.repository.EventRepository;
import com.event.booking.service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    private Event event;
    private EventRequest eventRequest;
    private ReserveRequest reserveRequest;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setEventDescription("Description");
        event.setCategory(EventCategory.CONFERENCE);
        event.setDate(LocalDate.now());
        event.setAvailableAttendeesCount(100);

        eventRequest = new EventRequest();
        eventRequest.setName("Test Event");
        eventRequest.setEventDescription("Description");
        eventRequest.setCategory(EventCategory.CONFERENCE);
        eventRequest.setDate(LocalDate.now());
        eventRequest.setAvailableAttendeesCount(100);

        reserveRequest = new ReserveRequest();
        reserveRequest.setAttendeesCount(10);
    }

    @Test
    void testGetAll() {
        when(eventRepository.findAll()).thenReturn(Arrays.asList(event));
        List<Event> events = eventService.getAll();
        assertEquals(1, events.size());
        verify(eventRepository, times(1)).findAll();
    }

    @Test
    void testFindEvents() {
        when(eventRepository.findEvents(anyString(), any(), any(), anyString())).thenReturn(Arrays.asList(event));
        List<Event> events = eventService.findEvents("Test Event", LocalDate.now(), LocalDate.now(), "Conference");
        assertEquals(1, events.size());
        verify(eventRepository, times(1)).findEvents(anyString(), any(), any(), anyString());
    }

    @Test
    void testCreate() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        Event createdEvent = eventService.create(eventRequest);
        assertEquals(event.getName(), createdEvent.getName());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testReserveEvent() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        Event reservedEvent = eventService.reserveEvent(1L, reserveRequest);
        assertEquals(110, reservedEvent.getAvailableAttendeesCount());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testReserveEventNotFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            eventService.reserveEvent(1L, reserveRequest);
        });
        assertEquals("Data not found", exception.getErrorMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testCancelReservation() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        event.setAvailableAttendeesCount(20);
        ReserveRequest cancelRequest = new ReserveRequest();
        cancelRequest.setAttendeesCount(10);
        Event updatedEvent = eventService.cancelReservation(1L, cancelRequest);
        assertEquals(10, updatedEvent.getAvailableAttendeesCount());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void testCancelReservationNotFound() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.empty());
        ReserveRequest cancelRequest = new ReserveRequest();
        cancelRequest.setAttendeesCount(10);
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            eventService.cancelReservation(1L, cancelRequest);
        });
        assertEquals("Data not found", exception.getErrorMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testCancelReservationInsufficientAttendees() {
        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(event));
        ReserveRequest cancelRequest = new ReserveRequest();
        cancelRequest.setAttendeesCount(110);
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            eventService.cancelReservation(1L, cancelRequest);
        });
        assertEquals("Available attendees is less than attendees canceling reservation", exception.getErrorMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getErrorCode());
        verify(eventRepository, times(1)).findById(anyLong());
        verify(eventRepository, never()).save(any(Event.class));
    }
}

