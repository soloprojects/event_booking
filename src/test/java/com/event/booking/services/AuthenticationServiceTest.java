package com.event.booking.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.event.booking.config.JwtService;
import com.event.booking.config.TokenType;
import com.event.booking.dto.AuthenticationRequest;
import com.event.booking.dto.AuthenticationResponse;
import com.event.booking.dto.RegisterRequest;
import com.event.booking.entity.Role;
import com.event.booking.entity.Token;
import com.event.booking.entity.User;
import com.event.booking.repository.TokenRepository;
import com.event.booking.repository.UserRepository;
import com.event.booking.service.AuthenticationService;
import com.event.booking.service.RoleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository repository;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private AuthenticationService authenticationService;

    private RegisterRequest registerRequest;
    private AuthenticationRequest authenticationRequest;
    private User user;
    private Role role;
    private Token token;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("John Doe");
        registerRequest.setEmail("john.doe@example.com");
        registerRequest.setPassword("password");
        registerRequest.setRole_id(1L);

        authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail("john.doe@example.com");
        authenticationRequest.setPassword("password");

        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
        user.setPassword("encodedPassword");
        user.setRoles(new HashSet<>(Set.of(role)));

        token = new Token();
        token.setId(1L);
        token.setToken("jwtToken");
        token.setUser(user);
        token.setTokenType(TokenType.BEARER);
        token.setExpired(false);
        token.setRevoked(false);
    }

    @Test
    void testRegister() {
        when(roleService.findById(anyLong())).thenReturn(role);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(repository.save(any(User.class))).thenReturn(user);
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.register(registerRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(repository, times(1)).save(any(User.class));
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testAuthenticate() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("jwtToken");
        when(jwtService.generateRefreshToken(any(User.class))).thenReturn("refreshToken");

        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);

        assertNotNull(response);
        assertEquals("jwtToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        verify(repository, times(1)).findByEmail(anyString());
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testSaveUserToken() {
        authenticationService.saveUserToken(user, "jwtToken");

        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testRevokeAllUserTokens() {
        when(tokenRepository.findAllValidTokenByUser(anyLong())).thenReturn(List.of(token));

        authenticationService.revokeAllUserTokens(user);

        assertTrue(token.isExpired());
        assertTrue(token.isRevoked());
        verify(tokenRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testRefreshToken() throws IOException {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("Bearer refreshToken");
        when(jwtService.extractUsername(anyString())).thenReturn("john.doe@example.com");
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(anyString(), any(User.class))).thenReturn(true);
        when(jwtService.generateToken(any(User.class))).thenReturn("newJwtToken");

        authenticationService.refreshToken(request, response);

        verify(repository, times(1)).findByEmail(anyString());
        verify(tokenRepository, times(1)).save(any(Token.class));
    }

    @Test
    void testLogout() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Authentication authentication = mock(Authentication.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer jwtToken");
        when(tokenRepository.findByToken(anyString())).thenReturn(Optional.of(token));

        authenticationService.logout(request, response, authentication);

        assertTrue(token.isExpired());
        assertTrue(token.isRevoked());
        verify(tokenRepository, times(1)).save(any(Token.class));
    }
}
