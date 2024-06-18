package com.event.booking.dto;

import com.event.booking.entity.EventCategory;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequest {

    @Size(max = 100)
    @NotNull(message = "Name of event shouldn't be null")
    @Schema(example= "Experience season 4", description = "Name of Event")
    private String name;

    @FutureOrPresent(message = "The date must be in the present or future.")
    @Schema(example= "2026-04-02", description = "Date of Event")
    private LocalDate date;

    @NotNull(message = "Available Attendees should be more than 1 and not more than 1000")
    @Min(1)
    @Max(1000)
    @Schema(example= "3", description = "Number of available attendees")
    private Integer availableAttendeesCount;

    @Size(max = 500)
    @NotNull(message = "Event description shouldn't be null")
    @Schema(example= "This will be a great event", description = "Description of Event")
    private String eventDescription;

    @NotNull(message = "Category of event shouldn't be null")
    @Schema(example= "CONCERT", description = "Category of Event")
    private EventCategory category;

}
