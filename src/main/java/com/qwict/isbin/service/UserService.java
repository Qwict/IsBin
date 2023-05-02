package com.qwict.isbin.service;

import com.qwict.isbin.dto.AuthUserDto;
import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.User;

import java.util.List;

public interface UserService {
        void saveUser(UserDto userDto);
        void updateUser(User user);
        User findUserByEmail(String email);
        List<UserDto> findAllUsers();
        UserDto mapToUserDto(User user);
        AuthUserDto mapToAuthenticatedUserDto(User user);
        AuthUserDto getAuthUserDto();
}
