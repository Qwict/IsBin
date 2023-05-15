package com.qwict.isbin.controller;

import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.LocationDto;
import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.User;
import com.qwict.isbin.repository.LocationRepository;
import com.qwict.isbin.service.AuthorService;
import com.qwict.isbin.service.BookService;
import com.qwict.isbin.service.LocationService;
import jakarta.validation.Valid;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {
    private final BookService bookService;
    private final AuthorService authorService;
    private final LocationService locationService;

    public BookController(
            BookService bookService, AuthorService authorService,
            LocationService locationService
    ) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.locationService = locationService;
    }

//    @RequestMapping("/book")
//    public String book(Model model) {
//        model.addAttribute("title", "ISBIN book");
//        model.addAttribute("message", "Welcome to the ISBIN book page!");
//
////            model.addAttribute("book", "book");
//
//        return "book";
//    }

    @RequestMapping("/admin/add-book")
    public String addBook(Model model) {
        BookDto book = new BookDto();
//        model.addAttribute("title", "ISBIN add book");
//        model.addAttribute("message", "Welcome to the ISBIN add book page!");
        model.addAttribute("activePage", "book");

        model.addAttribute("book", book);
        return "add-book";
    }

    @RequestMapping("/admin/edit-book/{id}")
    public String editBook(@PathVariable("id") String id, Model model) {
        Book bookFromDatabase = bookService.getById(id);

        BookDto book = bookService.mapToBookDto(bookFromDatabase);
//        model.addAttribute("title", "ISBIN edit book");
//        model.addAttribute("message", "Welcome to the ISBIN edit book page!");
        model.addAttribute("activePage", "book");

        model.addAttribute("book", book);
        return "add-book";
    }

    @RequestMapping(value = "/user/catalog")
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
        return "catalog";
    }

    @RequestMapping(value = "/book/{id}")
    public String getBookById(@PathVariable("id") String id, Model model) {
        model.addAttribute("activePage", "book");
        // throw a 404 if the book doesn't exist

        try {
            Long.parseLong(id);
        } catch (NumberFormatException e) {
            // TODO: make a BAD_REQUEST response page
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The id must be a number.");
        }
        if (id == null || id.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        Book bookFromDatabase;
        bookFromDatabase = bookService.getById(id);
        if (bookFromDatabase == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
        BookDto bookDto = bookService.mapToBookDto(bookFromDatabase);
        String coverURL = "/images/bookPlaceholder.jpg";

        model.addAttribute("book", bookDto);
        JSONObject response = bookService.getBookFromRemoteAPI(bookFromDatabase.getIsbn());

        JSONObject remoteBook = (JSONObject) response.get(String.format("ISBN:%s", bookFromDatabase.getIsbn()));
        if (remoteBook != null) {
            model.addAttribute("hasRemoteDetails", true);

            JSONObject details = (JSONObject) remoteBook.get("details");
            JSONArray covers = (JSONArray) details.get("covers");
            if (covers != null) {
                List<String> coverIds = new ArrayList<>();
                for (Object cover : covers) {
                    coverIds.add(cover.toString());
                }
                coverURL = String.format("https://covers.openlibrary.org/b/id/%s-L.jpg", coverIds.get(0));
            }

            // date part
            String remotePublishDate = (String) details.get("publish_date");
            model.addAttribute("remotePublishDate", remotePublishDate);

            // description part
            String remoteDescription = (String) details.get("description");
            model.addAttribute("remoteDescription", remoteDescription);
        } else {
            model.addAttribute("hasRemoteDetails", false);
        }

        model.addAttribute("coverURL", coverURL);
        return "book";
    }

    @RequestMapping(value = "/book/most-popular")
    public String index(Model model) {
        model.addAttribute("loggedIn", true);
        model.addAttribute("isAdmin", false);
        model.addAttribute("activePage", "book");

        model.addAttribute("title", "ISBIN Most Popular");
        model.addAttribute("message", "Welcome to the ISBIN Most Popular page!");

        List<BookDto> bookDtos = bookService.getMostPopularBookDtos();

        model.addAttribute("bookDtos", bookDtos);
        return "most-popular";
    }


//    TODO: add a search by isbn
//    @RequestMapping(value = "/book/search/isbn/{isbn}")


}
