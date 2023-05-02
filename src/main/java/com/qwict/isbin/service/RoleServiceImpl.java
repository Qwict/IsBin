package com.qwict.isbin.service;

import com.qwict.isbin.dto.RoleDto;
import com.qwict.isbin.model.Role;
import com.qwict.isbin.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public RoleDto mapToRoleDto(Role role) {
        return new RoleDto(role.getName().split("_")[1].toLowerCase());
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }
}
