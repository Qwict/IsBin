package com.qwict.isbin.controller;

import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.LocationDto;
import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.service.BookService;
import com.qwict.isbin.service.UserService;
import com.qwict.isbin.validator.BookDtoValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/owner")
public class OwnerController {
    @Autowired
    private UserService userService;

    @GetMapping("/registered-users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("activePage", "owner");
        return "owner/registered-users";
    }
}
