package com.qwict.isbin.controller;

import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.service.AuthorService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @RequestMapping("/author/{id}")
    public String getAuthorById(@PathVariable("id") String id, Model model) {
        model.addAttribute("title", "ISBIN author");
        model.addAttribute("message", "Welcome to the ISBIN author page!");
        model.addAttribute("activePage", "author");

        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The id must be a number.");
        }

        if (id == null || id.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }

        AuthorDto authorFromDatabase;
        authorFromDatabase = authorService.getAuthorDtoById(id);
        if (authorFromDatabase == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }

        model.addAttribute("author", authorFromDatabase);
        model.addAttribute("bookDtos", authorFromDatabase.getWritten());

        return "public/author";
    }
}
