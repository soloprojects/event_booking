package com.event.booking.iservice;

import com.event.booking.dto.RoleRequest;
import com.event.booking.entity.Role;
import java.util.List;

public interface IRole {
    Role findById(Long roleId);
    List<Role> getAll();
    Role saveRole(RoleRequest role);
}
