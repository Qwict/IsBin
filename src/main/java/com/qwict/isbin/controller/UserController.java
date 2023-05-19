package com.qwict.isbin.controller;


import com.qwict.isbin.domein.DomeinController;
import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.User;
import com.qwict.isbin.service.AuthorService;
import com.qwict.isbin.service.BookService;
import com.qwict.isbin.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/user")
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private BookService bookService;
    @Autowired
    private AuthorService authorService;
    @Autowired
    private DomeinController domeinController;

    @GetMapping("/book/{id}")
    public String favoriteBook(@PathVariable String id, Model model, HttpServletRequest request) {
        // Get the URL of the current page
        String referer = request.getHeader("Referer");

        User user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        Book book = bookService.findBookById(id);
        user.getBooks().stream().filter(b -> String.valueOf(b.getId()).equals(String.valueOf(book.getId())))
                .findFirst().ifPresentOrElse(
                        b -> {
//                            System.out.printf("Book %s is already in favorites%n", b.getTitle());
                            user.getBooks().remove(b);

                        },
                        () -> {
//                            System.out.printf("Book %s added to favorites%n", book.getTitle());
                            user.getBooks().add(book);
                        }
                );

        if (user.getMaxFavorites() < user.getBooks().size()) {
//            System.out.println("User has reached max favorites");
            return "redirect:" + referer;
        }

        userService.updateUser(user);
        model.addAttribute("authUser", userService.mapToAuthenticatedUserDto(user));

//        redirectAttributes.addFlashAttribute("successMessage", "Book added to favorites!");
        return "redirect:" + referer;
    }

    @RequestMapping("/favorites")
    public String favorites(Model model) {
        model.addAttribute("loggedIn", true);
        model.addAttribute("isAdmin", true);
        model.addAttribute("activePage", "book");

        model.addAttribute("title", "My Favorites");
        model.addAttribute("message", "This table represents all the books that you have added to favorites.");

        User user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        List<Book> books = user.getBooks();
        List<BookDto> bookDtos = books.stream().map(book -> bookService.mapToBookDto(book)).collect(Collectors.toList());
        model.addAttribute("bookDtos", bookDtos);
        return "user/favorites";
    }


    @RequestMapping(value = "/catalog")
    public String catalog(Model model) {
        model.addAttribute("activePage", "book");
        model.addAttribute("title", "ISBIN Catalog");
        model.addAttribute("message", "Welcome to the ISBIN Catalog page! This page is for logged in users.");

        List<BookDto> bookDtos = new ArrayList<>();
        List<Book> books = bookService.findAll();
        books.stream().forEach(book -> {
            bookDtos.add(bookService.mapToBookDto(book));
        });
        model.addAttribute("bookDtos", bookDtos);
        return "user/catalog";
    }

    @GetMapping("/search")
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
//            System.out.printf("The search term %s is not a number.\n", searchTerm);
        }

        model.addAttribute("activePage", "author");
        List<AuthorDto> authorDtos = authorService.searchAuthorsBySearchTerm(searchTerm);
        model.addAttribute("authorDtos", authorDtos);

        List<BookDto> bookDtos = bookService.searchBooksByAuthorDtos(authorDtos);
        model.addAttribute("bookDtos", bookDtos);
        return "user/search-results";
    }
}
