package com.qwict.isbin.controller;

import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.service.BookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class AdminController {
    @Autowired
    private BookService bookService;

    @PostMapping("/admin/book")
    public String registration(@Valid @ModelAttribute("book") BookDto bookDto,
                               BindingResult result,
                               Model model) {
        System.out.printf("bookDto: %s%n", bookDto);
        model.addAttribute("activePage", "book");




        bookService.saveBook(bookDto, result);
//            result.rejectValue("location", null,
//                    "This location is already taken. Please choose another one.");
//            model.addAttribute("book", bookDto);
//            return "/add-book";

        if (result.hasErrors()) {
            model.addAttribute("book", bookDto);
            return "/add-book";
        }
        return "redirect:/admin/add-book?success";

    }
}
