package com.event.booking.controller;

import com.event.booking.dto.AuthenticationRequest;
import com.event.booking.dto.AuthenticationResponse;
import com.event.booking.dto.RoleRequest;
import com.event.booking.entity.Role;
import com.event.booking.service.AuthenticationService;
import com.event.booking.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private RoleService roleService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController).build();
    }

    @Test
    void testAuthenticate() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test@example.com", "password");
        String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        AuthenticationResponse response = new AuthenticationResponse(accessToken, refreshToken);

        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("User Logged in successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.access_token").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.refresh_token").exists());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authenticationService, times(1)).authenticate(request);
    }

    @Test
    @WithMockUser(username="john",roles={"USER","ADMIN"})
    void testRefreshToken() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/refresh-token"))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(authenticationService, times(1)).refreshToken(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

    @Test
    void testLogout() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);

        ResponseEntity<Object> responseEntity = authenticationController.logout(request, response, authentication);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(authenticationService, times(1)).logout(request, response, authentication);
    }

    @Test
    @WithMockUser(username="john",roles={"USER","ADMIN"})
    void testCreateRole() throws Exception {
        RoleRequest request = new RoleRequest("ROLE_USER", "User role description");
        Role role = new Role(1L, "ROLE_USER", "User role description");

        when(roleService.saveRole(any(RoleRequest.class))).thenReturn(role);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/role/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Role created successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.name").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.description").value("User role description"));

        verify(roleService, times(1)).saveRole(request);
    }

    @Test
    @WithMockUser(username="john",roles={"USER","ADMIN"})
    void testGetRoles() throws Exception {
        List<Role> roles = List.of(new Role(1L, "ROLE_USER", "User role description"));

        when(roleService.getAll()).thenReturn(roles);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/auth/role/get"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("List of roles retrieved successfully"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value("ROLE_USER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description").value("User role description"));

        verify(roleService, times(1)).getAll();
    }
}


