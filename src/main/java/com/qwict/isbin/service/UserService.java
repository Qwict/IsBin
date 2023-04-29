package com.qwict.isbin.service;

import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.Role;
import com.qwict.isbin.model.User;

import java.util.List;

public interface UserService {
        void saveUser(UserDto userDto);
        void saveAdmin(UserDto userDto);
        void saveOwner(UserDto userDto);

        // Only for seeding!!
//        void saveUserWithRoles(UserDto userDto, List<Role> roles);

        User findUserByEmail(String email);

        List<UserDto> findAllUsers();
}
