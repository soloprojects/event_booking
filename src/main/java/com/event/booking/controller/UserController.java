package com.event.booking.controller;

import com.event.booking.dto.AuthenticationResponse;
import com.event.booking.dto.RegisterRequest;
import com.event.booking.service.AuthenticationService;
import com.event.booking.utility.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserController {
    private final AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<Object> registerUser(@Valid @RequestBody RegisterRequest userModel, final HttpServletRequest request) {
        AuthenticationResponse authResponse = authenticationService.register(userModel);

        return ResponseHandler.generateResponse("User created successfully", HttpStatus.CREATED, authResponse);
    }


}
