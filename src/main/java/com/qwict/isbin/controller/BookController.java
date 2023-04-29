package com.qwict.isbin.controller;

import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.User;
import com.qwict.isbin.service.BookService;
import jakarta.validation.Valid;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
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
        model.addAttribute("title", "ISBIN add book");
        model.addAttribute("message", "Welcome to the ISBIN add book page!");
        model.addAttribute("activePage", "book");

        return "add-book";
    }

    @PostMapping("/admin/book")
    public String registration(@Valid @ModelAttribute("book") BookDto bookDto,
                               BindingResult result,
                               Model model) {
        model.addAttribute("activePage", "book");
        model.addAttribute("title", "Add a Book");
        model.addAttribute("message", "An admin can add a book to the database.");

//        User existingUser = userService.findUserByEmail(userDto.getEmail());
        Book existingBook = bookService.findBookByIsbn(bookDto.getIsbn());


//        if(existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()){
//            result.rejectValue("email", null,
//                    "There is already an account registered with the same email");
//        }

//        if(result.hasErrors()){
//            model.addAttribute("user", userDto);
//            return "/register";
//        }

//        userService.saveUser(userDto);
        return "redirect:/admin/book?success";

    }

    @RequestMapping(value = "/book/{id}")
    public String getBookById(@PathVariable("id") String id, Model model) {
        Book bookFromDatabase = bookService.getById(id);
        String coverURL = "/images/bookPlaceholder.jpg";
        model.addAttribute("book", bookFromDatabase);
        JSONObject response = bookService.getBookFromRemoteAPI(bookFromDatabase.getIsbn());

        JSONObject remoteBook = (JSONObject) response.get(String.format("ISBN:%s", bookFromDatabase.getIsbn()));
        if (remoteBook != null) {
            model.addAttribute("hasRemoteDetails", true);

            System.out.printf("remoteBook: %s\n", remoteBook);
            JSONObject details = (JSONObject) remoteBook.get("details");
            System.out.printf("details: %s\n", details);
            JSONArray covers = (JSONArray) details.get("covers");
            if (covers != null) {
                System.out.printf("covers: %s\n", covers);
                List<String> coverIds = new ArrayList<>();
                for (Object cover : covers) {
                    coverIds.add(cover.toString());
                }
                System.out.printf("coverIds: %s\n", coverIds);
                coverURL = String.format("https://covers.openlibrary.org/b/id/%s-L.jpg", coverIds.get(0));
            }

            // date part
            String remotePublishDate = (String) details.get("publish_date");
            System.out.printf("remotePublishDate: %s\n", remotePublishDate);
            model.addAttribute("remotePublishDate", remotePublishDate);

            // description part
            String remoteDescription = (String) details.get("description");
            System.out.printf("remoteDescription: %s\n", remoteDescription);
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

        List<Book> books = bookService.findAll();
            model.addAttribute("books", books);
        return "most-popular";
}

}
