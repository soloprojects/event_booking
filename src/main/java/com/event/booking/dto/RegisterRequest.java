package com.event.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  @Schema(example= "Solomon Nweze", description = "Name of user")
  @NotNull(message = "Name shouldn't be null")
  @Size(max = 100, message = "Name must be less than or equal to 100 characters")
  public String name;

  @Schema(example= "solomonnweze@gmail.com", description = "email of user")
  @Email(message = "Firstname shouldn't be null")
  public String email;

  @Size(min = 8, message = "Password must be 8 or more than 8 characters")
  @NotNull(message = "Password shouldn't be null")
  @Schema(example= "password", description = "password of user")
  public String password;

  @NotNull(message = "User role shouldn't be null")
  @Schema(example= "1", description = "Role of user")
  public Long role_id;
}
