package com.qwict.isbin.controller;

import com.qwict.isbin.model.Book;
import com.qwict.isbin.service.BookService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping("/book")
    public String book(Model model) {
        model.addAttribute("title", "ISBIN book");
        model.addAttribute("message", "Welcome to the ISBIN book page!");

//            model.addAttribute("book", "book");

        return "book";
    }

//    @RequestMapping(value = "/ex/foos/{id}")
//    public String getFoosBySimplePathWithPathVariable(
//            @PathVariable("id") long id, Model model) {
//        model.addAttribute("foos", bookService.getBookById(id));
//        return "foos";
//    }

}
