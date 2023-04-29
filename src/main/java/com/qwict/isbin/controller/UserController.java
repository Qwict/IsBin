package com.qwict.isbin.controller;


import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.User;
import com.qwict.isbin.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class UserController {

    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("activePage", "login");
        model.addAttribute("title", "ISBIN login");
        model.addAttribute("message", "Welcome to the ISBIN login page!");
        return "login";
    }

    @RequestMapping("/register")
    public String register(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("activePage", "register");
        model.addAttribute("title", "ISBIN register");
        model.addAttribute("message", "Welcome to the ISBIN register page!");

        model.addAttribute("user", user);
        return "register";
    }

    // handler method to handle user registration form submit request
    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        User existingUser = userService.findUserByEmail(userDto.getEmail());
        model.addAttribute("activePage", "register");
        model.addAttribute("title", "ISBIN register");
        model.addAttribute("message", "Welcome to the ISBIN register page!");

        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }


    // handler method to handle list of users

    @PreAuthorize("hasRole('ROLE_OWNER')")
    @GetMapping("/owner/users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "registered-users";
    }
}
