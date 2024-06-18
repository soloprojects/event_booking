package com.event.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

  @Schema(example= "solomonnweze@gmail.com", description = "email of user")
  @Email(message = "Firstname shouldn't be null")
  public String email;

  @Schema(example= "password", description = "password of user")
  @NotNull(message = "Please enter your password")
  public String password;
}
