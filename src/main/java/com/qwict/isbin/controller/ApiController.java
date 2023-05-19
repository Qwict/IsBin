package com.qwict.isbin.controller;

import com.qwict.isbin.IsBinApplication;
import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.service.AuthorService;
import com.qwict.isbin.service.BookService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private AuthorService authorService;

    @Autowired
    private BookService bookService;

    @GetMapping("/public/health/version")
    public JSONObject getVersion() {
        JSONObject response = new JSONObject();
        response.put("env", IsBinApplication.getEnv());
        response.put("version", IsBinApplication.getVersion());
        response.put("name", IsBinApplication.getName());
        return response;
    }

    @GetMapping("/public/health/ping")
    public JSONObject getStatus() {
        JSONObject response = new JSONObject();
        response.put("pong", true);
        return response;
    }

    @GetMapping("/public/author/{firstname}/{lastname}")
    public ResponseEntity<AuthorDto> getAuthor(
            @PathVariable("firstname") String firstname,
            @PathVariable("lastname") String lastname
    ) {
        try {
            return ResponseEntity
                    .ok()
                    .body(authorService.getByFirstNameAndLastName(firstname, lastname));
        } catch (IllegalArgumentException iae) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/public/book/{isbn}")
    public ResponseEntity<BookDto> getBook(@PathVariable("isbn") String isbn) {
        Book book = bookService.findBookByIsbn(isbn);
        if (book == null) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
        return ResponseEntity
                .ok()
                .body(bookService.mapToBookDto(book));
    }

}
