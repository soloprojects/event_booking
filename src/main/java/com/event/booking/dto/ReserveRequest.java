package com.event.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReserveRequest {

    @NotNull(message = "Available Attendees should be more than 1 and not more than 1000")
    @Min(1)
    @Max(1000)
    @Schema(example= "7", description = "Number of available attendees")
    private Integer attendeesCount;
}
