package com.qwict.isbin.controller;

import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.LocationDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.service.BookService;
import com.qwict.isbin.validator.BookDtoValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private BookService bookService;
    @Autowired
    private BookDtoValidator validator;

    @RequestMapping("/add-book")
    public String addBook(Model model) {
        BookDto book = new BookDto();
//        model.addAttribute("title", "ISBIN add book");
//        model.addAttribute("message", "Welcome to the ISBIN add book page!");
        model.addAttribute("activePage", "book");

        List<AuthorDto> authorDtos = new ArrayList<>();
        for (int i = 0; i < 3; i++){
            authorDtos.add(new AuthorDto());
        }

        book.setAuthorDtos(authorDtos);

        List<LocationDto> locationDtos = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            locationDtos.add(new LocationDto());
        }

        book.setLocationDtos(locationDtos);

        model.addAttribute("book", book);
        return "admin/add-book";
    }

    @RequestMapping("/edit-book/{id}")
    public String editBook(@PathVariable("id") String id, Model model) {
        Book bookFromDatabase = bookService.getById(id);

        BookDto book = bookService.mapToBookDto(bookFromDatabase);
        book.setPrice(bookFromDatabase.getPrice().toString());
//        model.addAttribute("title", "ISBIN edit book");
//        model.addAttribute("message", "Welcome to the ISBIN edit book page!");
        model.addAttribute("activePage", "book");

        if (book.getAuthorDtos() == null) {
            book.setAuthorDtos(new ArrayList<>());
        }
        while (book.getAuthorDtos().size() < 3) {
            book.getAuthorDtos().add(new AuthorDto());
        }

        if (book.getLocationDtos() == null) {
            book.setLocationDtos(new ArrayList<>());
        }
        while (book.getLocationDtos().size() < 3) {
            book.getLocationDtos().add(new LocationDto());
        }

        model.addAttribute("book", book);
        return "admin/add-book";
    }

    @PostMapping("/add-book")
    public String registration(
            @Valid @ModelAttribute("book") BookDto bookDto,
            BindingResult result,
            Model model,
            HttpServletRequest request
    ) {
        String referer = request.getHeader("Referer");
        model.addAttribute("activePage", "book");

        String[] splitReferer = referer.split("/");
        boolean isEdit = Objects.equals(splitReferer[splitReferer.length - 2], "edit-book");
        // for now there is no validation on edit
        if (!isEdit) {
            validator.validate(bookDto, result);
        }
        if (result.hasErrors()) {
            if (isEdit) {
                // This does not seem to work (validation also doesn't work on edit)
//                return String.format("admin/edit-book/%s", splitReferer[splitReferer.length - 1]);
                return String.format("redirect:%s?error", referer);
            }
            return "admin/add-book";
        }

        try {
            bookService.saveBook(bookDto, result, isEdit);
        } catch (Exception e) {
            e.printStackTrace();
            // for testing
//            return "/add-book";
            // for production
            if (isEdit) {
                return String.format("redirect:%s?error", referer);
            }
            return String.format("redirect:/admin/add-book?error&errorMessage=%s", e.getMessage());
        }
        return "redirect:/admin/add-book?success";

    }
}
