package com.qwict.isbin.controller;


import com.qwict.isbin.dto.AuthUserDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.User;
import com.qwict.isbin.service.BookService;
import com.qwict.isbin.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private UserService userService;
    private BookService bookService;

    public UserController(UserService userService, BookService bookService){
        this.userService = userService;
        this.bookService = bookService;
    }

    @PostMapping("/user/book/{id}")
    public ModelAndView addBookToFavorites(@PathVariable String id){

        User user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());

        Book book = bookService.findBookById(id);

        user.getBooks().stream().filter(b -> String.valueOf(b.getId()).equals(String.valueOf(book.getId())))
            .findFirst().ifPresentOrElse(
                b -> {
                    System.out.printf("Book %s is already in favorites%n", b.getTitle());
                    user.getBooks().remove(b);

                },
                () -> {
                    System.out.printf("Book %s added to favorites%n", book.getTitle());
                    user.getBooks().add(book);
                }
        );

        if (user.getMaxFavorites() < user.getBooks().size()) {
            System.out.println("User has reached max favorites");
            return new ModelAndView("redirect:/max-favorites-reached");
//            return "redirect:/max-favorites-reached";
        }

        userService.updateUser(user);
        ModelAndView mv = new ModelAndView("redirect:/user/favorites");
        mv.addObject("authUser", userService.mapToAuthenticatedUserDto(user));
        return mv;

//        user.addBookToFavorites(book);
//        userService.saveUser(userService.mapToUserDto(user));
//        return "redirect:/";
//        return "redirect:/user/favorites";
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
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/login";
    }

    @RequestMapping("/user/favorites")
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
        return "favorites";
    }

    @GetMapping("/owner/registered-users")
    public String users(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("activePage", "owner");
        return "registered-users";
    }
}
