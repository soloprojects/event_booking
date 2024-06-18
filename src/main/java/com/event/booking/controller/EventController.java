package com.event.booking.controller;

import com.event.booking.dto.EventRequest;
import com.event.booking.dto.ReserveRequest;
import com.event.booking.entity.Event;
import com.event.booking.service.EventService;
import com.event.booking.utility.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;

    @GetMapping("view")
    public ResponseEntity<Object> getAllEvents() {
        List<Event> events = eventService.getAll();
        return ResponseHandler.generateResponse("Events selected successfully", HttpStatus.OK, events);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findEvents(
            @RequestParam(required = false) String name,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String category) {

        List<Event> events = eventService.findEvents(name, startDate, endDate, category);
        return ResponseHandler.generateResponse("Search completed successfully", HttpStatus.OK, events);
    }

    @PostMapping
    public ResponseEntity<Object> createEvent(@RequestBody EventRequest eventRequest) {
        Event createdEvent = eventService.create(eventRequest);
        return ResponseHandler.generateResponse("Event created successfully", HttpStatus.OK, createdEvent);
    }

    @PostMapping("/{id}/tickets")
    public ResponseEntity<Object> reserveEvent(@PathVariable Long id, @RequestBody ReserveRequest reserveRequest) {
        Event reservedEvent = eventService.reserveEvent(id, reserveRequest);
        return ResponseHandler.generateResponse("tickets created successfully", HttpStatus.OK, reservedEvent);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Object> cancelReservation(@PathVariable Long id, @RequestBody ReserveRequest cancelRequest) {
        Event cancelledEvent = eventService.cancelReservation(id, cancelRequest);
        return ResponseHandler.generateResponse("User created successfully", HttpStatus.OK, cancelledEvent);
    }
}
