package com.qwict.isbin.controller;

import com.qwict.isbin.dto.ChangeUserDto;
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


    // Somehow the use of the @PathVariable makes sure that the id is not reset to 0 (because primary datatype)
    // I do not use the {id} anywhere but because of it the id is not reset to 0 (maybe because of form in html)
    @PostMapping("/edit-user/{id}")
    public String editUser(@ModelAttribute("user") ChangeUserDto user){
        try {
            userService.updateUserWithChangeUserDto(user);
        } catch (Exception e) {
            return String.format("redirect:/owner/registered-users?error&errorMessage=%s", e.getMessage());
        }
        return "redirect:/owner/registered-users";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id){
        try {
            userService.deleteUserById(id);
        } catch (Exception e) {
            return String.format("redirect:/owner/registered-users?error&errorMessage=%s", e.getMessage());
        }
        return "redirect:/owner/registered-users";
    }


}
