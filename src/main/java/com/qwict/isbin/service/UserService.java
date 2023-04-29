package com.qwict.isbin.service;

import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.User;

import java.util.List;

public interface UserService {
        void saveUser(UserDto userDto);

        User findUserByEmail(String email);

        List<UserDto> findAllUsers();
}
