package com.qwict.isbin.controller;

import com.qwict.isbin.model.Book;
import com.qwict.isbin.repository.BookRepository;
import com.qwict.isbin.service.BookService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class ScreenController {
    private final BookService bookService;

    public ScreenController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping("/home")
    public String home(Model model) {
        model.addAttribute("loggedIn", true);
        model.addAttribute("isAdmin", false);
        model.addAttribute("activePage", "home");

        model.addAttribute("title", "ISBIN Home");
        model.addAttribute("message", "Welcome to the ISBIN home page!");

        List<Book> books = bookService.findAll();
        System.out.printf("books: %s\n", books);
        model.addAttribute("books", books);
        return "home";
    }

    @RequestMapping
    public String index(Model model) {
        model.addAttribute("loggedIn", true);
        model.addAttribute("isAdmin", false);
        model.addAttribute("activePage", "home");

        model.addAttribute("title", "ISBIN Home");
        model.addAttribute("message", "Welcome to the ISBIN home page!");

        List<Book> books = bookService.findAll();
        System.out.printf("books: %s\n", books);
        model.addAttribute("books", books);
        return "home";
    }

    @RequestMapping("/user/favorites")
    public String favorites(Model model) {
        model.addAttribute("loggedIn", true);
        model.addAttribute("isAdmin", true);
        model.addAttribute("activePage", "book");

        model.addAttribute("title", "My Favorites");
        model.addAttribute("message", "This table represents all the books that you have added to favorites.");
        return "favorites";
    }



}
