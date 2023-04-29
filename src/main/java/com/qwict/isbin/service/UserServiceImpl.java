package com.qwict.isbin.service;

import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.Role;
import com.qwict.isbin.model.User;
import com.qwict.isbin.repository.RoleRepository;
import com.qwict.isbin.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role1 = roleRepository.findByName("ROLE_OWNER");
        if(role1 == null){
            role1 = checkRoleExist("ROLE_OWNER");
        }

        Role role2 = roleRepository.findByName("ROLE_ADMIN");
        if(role2 == null){
            role2 = checkRoleExist("ROLE_ADMIN");
        }

        Role role3 = roleRepository.findByName("ROLE_USER");
        if(role3 == null){
            role3 = checkRoleExist("ROLE_USER");
        }
        user.setRoles(Arrays.asList(role1, role2, role3));
        userRepository.save(user);
    }

    @Override
    public void saveAdmin(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleRepository.findByName("ROLE_ADMIN");
        if(role == null){
            role = checkRoleExist("ROLE_ADMIN");
        }
        user.setRoles(Arrays.asList(role));
        userRepository.save(user);
    }

    @Override
    public void saveOwner(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role1 = roleRepository.findByName("ROLE_OWNER");
        if(role1 == null){
            role1 = checkRoleExist("ROLE_OWNER");
        }

        Role role2 = roleRepository.findByName("ROLE_ADMIN");
        if(role2 == null){
            role2 = checkRoleExist("ROLE_ADMIN");
        }

        Role role3 = roleRepository.findByName("ROLE_USER");
        if(role3 == null){
            role3 = checkRoleExist("ROLE_USER");
        }
        user.setRoles(Arrays.asList(role1, role2, role3));
        userRepository.save(user);
    }


    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    private UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private Role checkRoleExist(String roleName){
        Role role = new Role();
        role.setName(roleName);
        return roleRepository.save(role);
    }
}
