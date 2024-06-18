package com.event.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleRequest {

    @Schema(example= "Admin", description = "name of role")
    @NotNull(message = "name shouldn't be null")
    public String name;

    @Schema(example= "Admin role in the app", description = "name of role")
    @NotNull(message = "Please enter description")
    public String description;

}
