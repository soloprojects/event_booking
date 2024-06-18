package com.event.booking.service;

import com.event.booking.dto.RoleRequest;
import com.event.booking.entity.Role;
import com.event.booking.exception.BusinessException;
import com.event.booking.iservice.IRole;
import com.event.booking.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService implements IRole {

    private final RoleRepository roleRepository;

    @Override
    public Role findById(Long roleId){
        return roleRepository.findById(roleId).orElseThrow(() -> new BusinessException("Role not found", HttpStatus.NOT_FOUND));
    }

    @Override
    public List<Role> getAll(){
        return roleRepository.findAll();
    }

    @Override
    public Role saveRole(RoleRequest role){
        Role roleBuilder = Role.builder()
                .name(role.getName())
                .description(role.getDescription())
                .build();
        roleRepository.save(roleBuilder);
        return roleBuilder;
    }

}
