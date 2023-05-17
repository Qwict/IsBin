package com.qwict.isbin.service;

import com.qwict.isbin.dto.AuthUserDto;
import com.qwict.isbin.dto.ChangeUserDto;
import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.User;

import java.util.List;

public interface UserService {
        void saveUser(UserDto userDto);
        void updateUser(User user);
        void updateUserWithChangeUserDto(ChangeUserDto updatedUser);
        User findUserByEmail(String email);
        List<UserDto> findAllUsers();
        List<ChangeUserDto> getAllChangeUserDtos();
        ChangeUserDto getChangeUserDtoById(Long id);
        UserDto mapToUserDto(User user);
        AuthUserDto mapToAuthenticatedUserDto(User user);
        AuthUserDto getAuthUserDto();
}
