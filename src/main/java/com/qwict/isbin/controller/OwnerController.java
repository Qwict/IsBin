package com.qwict.isbin.controller;

import com.qwict.isbin.dto.ChangeUserDto;
import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    private UserService userService;

    @GetMapping("/registered-users")
    public String users(Model model){
        List<ChangeUserDto> users = userService.getAllChangeUserDtos();
        model.addAttribute("users", users);
        model.addAttribute("activePage", "owner");
        return "owner/registered-users";
    }

    @RequestMapping("/edit-user/{id}")
    public String editUser(@PathVariable(name = "id") Long id, Model model){
        ChangeUserDto user = userService.getChangeUserDtoById(id);
        model.addAttribute("user", user);
        model.addAttribute("activePage", "owner");
        return "owner/edit-user";
    }

    @PostMapping("/edit-user")
    public String editUser(@ModelAttribute("user") ChangeUserDto user){
        userService.updateUserWithChangeUserDto(user);
        return "redirect:/owner/registered-users";
    }


}
