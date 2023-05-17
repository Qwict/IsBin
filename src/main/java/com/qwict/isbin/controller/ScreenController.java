package com.qwict.isbin.controller;

import com.qwict.isbin.domein.DomeinController;
import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.User;
import com.qwict.isbin.service.AuthorService;
import com.qwict.isbin.service.BookService;
import com.qwict.isbin.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class ScreenController {
    @Autowired
    private UserService userService;

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;


    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("loggedIn", true);
        model.addAttribute("isAdmin", false);
        model.addAttribute("activePage", "home");

        model.addAttribute("title", "ISBIN Home");
        model.addAttribute("message", "Welcome to the ISBIN home page!");

        List<BookDto> bookDtos = new ArrayList<>();
        List<Book> books = bookService.findAll();
        for (Book book : books) {
            bookDtos.add(bookService.mapToBookDto(book));
        }
        model.addAttribute("bookDtos", bookDtos);
        return "public/home";
    }

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("loggedIn", true);
        model.addAttribute("isAdmin", false);
        model.addAttribute("activePage", "home");

        model.addAttribute("title", "ISBIN Home");
        model.addAttribute("message", "Welcome to the ISBIN home page!");

        List<BookDto> bookDtos = new ArrayList<>();
        List<Book> books = bookService.findAll();
        for (Book book : books) {
            bookDtos.add(bookService.mapToBookDto(book));
        }
        model.addAttribute("bookDtos", bookDtos);
        return "public/home";
    }

    @RequestMapping("/login")
    public String login(Model model) {
        model.addAttribute("activePage", "login");
        model.addAttribute("title", "ISBIN login");
        model.addAttribute("message", "Welcome to the ISBIN login page!");
        return "public/login";
    }

    @RequestMapping("/register")
    public String register(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("activePage", "register");
        model.addAttribute("title", "ISBIN register");
        model.addAttribute("message", "Welcome to the ISBIN register page!");

        model.addAttribute("user", user);
        return "public/register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto,
                               BindingResult result,
                               Model model){
        model.addAttribute("activePage", "register");
        model.addAttribute("title", "ISBIN register");
        model.addAttribute("message", "Welcome to the ISBIN register page!");

        User existingUser = userService.findUserByEmail(userDto.getEmail());
        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
            result.rejectValue("email", null,
                    "There is already an account registered with the same email");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "public/register";
        }

        userService.saveUser(userDto);
        return "redirect:/login";
    }

}
