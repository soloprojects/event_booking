package com.event.booking.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.event.booking.dto.EventRequest;
import com.event.booking.dto.ReserveRequest;
import com.event.booking.entity.Event;
import com.event.booking.entity.EventCategory;
import com.event.booking.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Autowired
    private ObjectMapper objectMapper;

    private Event event1;
    private Event event2;
    private EventRequest eventRequest;
    private ReserveRequest reserveRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.standaloneSetup(eventController).build();

        event1 = new Event();
        event1.setId(1L);
        event1.setName("Event 1");
        event1.setEventDescription("Event 1 Description");
        event1.setCategory(EventCategory.CONFERENCE);
        event1.setDate(LocalDate.of(2023, 6, 14));
        event1.setAvailableAttendeesCount(100);

        event2 = new Event();
        event2.setId(2L);
        event2.setName("Event 2");
        event2.setEventDescription("Event 2 Description");
        event2.setCategory(EventCategory.CONCERT);
        event2.setDate(LocalDate.of(2023, 7, 14));
        event2.setAvailableAttendeesCount(200);

        eventRequest = new EventRequest();
        eventRequest.setName("New Event");
        eventRequest.setEventDescription("Event Request Description");
        eventRequest.setCategory(EventCategory.CONFERENCE);
        eventRequest.setDate(LocalDate.of(2023, 8, 14));
        eventRequest.setAvailableAttendeesCount(300);

        reserveRequest = new ReserveRequest();
        reserveRequest.setAttendeesCount(10);
    }

    @Test
    @WithMockUser(username="john",roles={"USER","ADMIN"})
    void testGetAllEvents() throws Exception {
        List<Event> events = Arrays.asList(event1, event2);

        when(eventService.getAll()).thenReturn(events);

        mockMvc.perform(get("/api/v1/events/view"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("Event 1"))
                .andExpect(jsonPath("$.data[1].name").value("Event 2"));
    }

    @Test
    @WithMockUser(username="john",roles={"USER","ADMIN"})
    void testFindEvents() throws Exception {
        List<Event> events = Arrays.asList(event1, event2);

        when(eventService.findEvents(any(String.class), any(LocalDate.class), any(LocalDate.class), any(String.class))).thenReturn(events);

        mockMvc.perform(get("/api/v1/events/search")
                        .param("name", "Event")
                        .param("startDate", "2023-06-01")
                        .param("endDate", "2023-06-30")
                        .param("category", "Category"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").value("Event 1"))
                .andExpect(jsonPath("$.data[1].name").value("Event 2"));
    }

    @Test
    @WithMockUser(username="john",roles={"USER","ADMIN"})
    void testCreateEvent() throws Exception {
        when(eventService.create(any(EventRequest.class))).thenReturn(event1);

        mockMvc.perform(post("/api/v1/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Event 1"));
    }

    @Test
    @WithMockUser(username="john",roles={"USER","ADMIN"})
    void testReserveEvent() throws Exception {
        when(eventService.reserveEvent(anyLong(), any(ReserveRequest.class))).thenReturn(event1);

        mockMvc.perform(post("/api/v1/events/1/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserveRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Event 1"));
    }

    @Test
    @WithMockUser(username="john",roles={"USER","ADMIN"})
    void testCancelReservation() throws Exception {
        when(eventService.cancelReservation(anyLong(), any(ReserveRequest.class))).thenReturn(event1);

        mockMvc.perform(post("/api/v1/events/1/cancel")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reserveRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Event 1"));
    }
}

