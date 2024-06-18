package com.event.booking.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import com.event.booking.entity.User;
import com.event.booking.exception.BusinessException;
import com.event.booking.repository.UserRepository;
import com.event.booking.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");
    }

    @Test
    void testFindByEmail_Success() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail("john.doe@example.com");

        assertNotNull(foundUser);
        assertEquals("john.doe@example.com", foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(anyString());
    }

    @Test
    void testFindByEmail_NotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            userService.findByEmail("john.doe@example.com");
        });

        assertEquals("Email not found", thrown.getErrorMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getErrorCode());
        verify(userRepository, times(1)).findByEmail(anyString());
    }
}

