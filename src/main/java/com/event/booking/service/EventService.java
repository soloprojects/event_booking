package com.event.booking.service;

import com.event.booking.dto.EventRequest;
import com.event.booking.dto.ReserveRequest;
import com.event.booking.entity.Event;
import com.event.booking.exception.BusinessException;
import com.event.booking.iservice.IEvent;
import com.event.booking.repository.EventRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@Service
public class EventService implements IEvent {

    private final EventRepository eventRepository;

    @Override
    public List<Event> getAll() {
        return eventRepository.findAll();
    }

    public List<Event> findEvents(String name, LocalDate startDate, LocalDate endDate, String category) {
        return eventRepository.findEvents(name, startDate, endDate, category);
    }

    @Override
    public Event create(@Valid EventRequest event) {
        Event newEvent = new Event();
        newEvent.setName(event.getName());
        newEvent.setEventDescription(event.getEventDescription());
        newEvent.setCategory(event.getCategory());
        newEvent.setDate(event.getDate());
        newEvent.setAvailableAttendeesCount(event.getAvailableAttendeesCount());
        return eventRepository.save(newEvent);
    }

    @Override
    public Event reserveEvent(Long eventId, @Valid ReserveRequest reserveRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException("Data not found", HttpStatus.NOT_FOUND));
        int currentAttendeeCount = event.getAvailableAttendeesCount();
        int newAttendeeCount = reserveRequest.getAttendeesCount() + currentAttendeeCount;
        event.setAvailableAttendeesCount(newAttendeeCount);
        eventRepository.save(event);
        return event;
    }

    @Override
    public Event cancelReservation(Long eventId, @Valid ReserveRequest cancelRequest) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new BusinessException("Data not found", HttpStatus.NOT_FOUND));

        int currentAttendeeCount = event.getAvailableAttendeesCount();
        int requestAttendeeCount = cancelRequest.getAttendeesCount();
        if(currentAttendeeCount < requestAttendeeCount){
            throw new BusinessException("Available attendees is less than attendees canceling reservation", HttpStatus.BAD_REQUEST);
        }
        int newAttendeeCount = currentAttendeeCount - cancelRequest.getAttendeesCount();
        event.setAvailableAttendeesCount(newAttendeeCount);
        eventRepository.save(event);
        return event;
    }

}
