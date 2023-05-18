package com.qwict.isbin.service;

import com.qwict.isbin.dto.RoleDto;
import com.qwict.isbin.model.Role;

import java.util.List;

public interface RoleService {
    RoleDto mapToRoleDto(Role role);
    Role findRoleByName(String name);
    Role saveRole(Role role);

    List<Role> findAllRoles();
}
