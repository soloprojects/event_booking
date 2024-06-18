package com.event.booking.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import com.event.booking.dto.RoleRequest;
import com.event.booking.entity.Role;
import com.event.booking.exception.BusinessException;
import com.event.booking.repository.RoleRepository;
import com.event.booking.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    private Role role;
    private RoleRequest roleRequest;

    @BeforeEach
    void setUp() {
        role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");
        role.setDescription("Standard user role");

        roleRequest = new RoleRequest();
        roleRequest.setName("ROLE_ADMIN");
        roleRequest.setDescription("Administrator role");
    }

    @Test
    void testFindById_Success() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(role));

        Role foundRole = roleService.findById(1L);

        assertNotNull(foundRole);
        assertEquals("ROLE_USER", foundRole.getName());
        verify(roleRepository, times(1)).findById(anyLong());
    }

    @Test
    void testFindById_NotFound() {
        when(roleRepository.findById(anyLong())).thenReturn(Optional.empty());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            roleService.findById(1L);
        });

        assertEquals("Role not found", thrown.getErrorMessage());
        assertEquals(HttpStatus.NOT_FOUND, thrown.getErrorCode());
        verify(roleRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAll() {
        Role role1 = new Role();
        role1.setId(1L);
        role1.setName("ROLE_USER");
        role1.setDescription("Standard user role");

        Role role2 = new Role();
        role2.setId(2L);
        role2.setName("ROLE_ADMIN");
        role2.setDescription("Administrator role");

        List<Role> roles = Arrays.asList(role1, role2);

        when(roleRepository.findAll()).thenReturn(roles);

        List<Role> foundRoles = roleService.getAll();

        assertNotNull(foundRoles);
        assertEquals(2, foundRoles.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testSaveRole() {
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        Role savedRole = roleService.saveRole(roleRequest);

        assertNotNull(savedRole);
        assertEquals("ROLE_ADMIN", savedRole.getName());
        verify(roleRepository, times(1)).save(any(Role.class));
    }
}

