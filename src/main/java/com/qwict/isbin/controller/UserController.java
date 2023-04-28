package com.qwict.isbin.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("activePage", "login");
        model.addAttribute("title", "ISBIN login");
        model.addAttribute("message", "Welcome to the ISBIN login page!");
        return "login";
    }

    @RequestMapping("/register")
    public String register(Model model) {
        model.addAttribute("activePage", "register");
        model.addAttribute("title", "ISBIN register");
        model.addAttribute("message", "Welcome to the ISBIN register page!");
        return "register";
    }
}
