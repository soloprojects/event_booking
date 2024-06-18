package com.event.booking.iservice;

import com.event.booking.dto.EventRequest;
import com.event.booking.dto.ReserveRequest;
import com.event.booking.entity.Event;

import java.time.LocalDate;
import java.util.List;

public interface IEvent {
    List<Event> getAll();
    List<Event> findEvents(String name, LocalDate startDate, LocalDate endDate, String category);
    Event create(EventRequest event);
    Event reserveEvent(Long eventId, ReserveRequest reserveRequest);
    Event cancelReservation(Long eventId, ReserveRequest reserveRequest);

}
