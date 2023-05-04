package com.qwict.isbin.controller;

import com.qwict.isbin.domein.DomeinController;
import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.service.AuthorService;
import com.qwict.isbin.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/")
public class ScreenController {
    private final BookService bookService;
    private final AuthorService authorService;

    private final DomeinController domeinController = new DomeinController();
    public ScreenController(BookService bookService, AuthorService authorService) {
        this.bookService = bookService;
        this.authorService = authorService;
    }

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
        return "home";
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
        return "home";
    }


    @GetMapping("/search/")
    public String search(
            @RequestParam String searchTerm,
            Model model
    ) {
        model.addAttribute("activePage", "book");
        model.addAttribute("title", "ISBIN Search");
        model.addAttribute("message", "Welcome to the ISBIN Search page!");
        model.addAttribute("searchTerm", searchTerm);

        // check if searchTerm can be parsed to a number
        try {
            String searchTermNoSpaceNoDash = domeinController.formatISBNToString(searchTerm);
            Long.parseLong(searchTermNoSpaceNoDash);
            Book book = bookService.findBookByIsbn(searchTermNoSpaceNoDash);
            if (book != null) {
                model.addAttribute("activePage", "book");
                return String.format("redirect:/book/%s", book.getId());
            } else {
                model.addAttribute("searchTerm", searchTerm);
                return String.format("redirect:/book/%s", searchTermNoSpaceNoDash);
            }
        } catch (NumberFormatException e) {
            System.out.printf("The search term %s is not a number.\n", searchTerm);
        }

        model.addAttribute("activePage", "author");
        List<AuthorDto> authorDtos = authorService.searchAuthorsBySearchTerm(searchTerm);
        model.addAttribute("authorDtos", authorDtos);

        List<BookDto> bookDtos = bookService.searchBooksByAuthorDtos(authorDtos);
        model.addAttribute("bookDtos", bookDtos);
        return "/search/search-results";
    }


}
