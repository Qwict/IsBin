package com.qwict.isbin.service;

import com.qwict.isbin.domein.RemoteAPI;
import com.qwict.isbin.dto.AuthUserDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.repository.BookRepository;
import org.json.simple.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;
    private UserService userService;

    public BookServiceImpl(BookRepository bookRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public Book findBookById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public void saveBook(BookDto bookDto) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setIsbn(bookDto.getIsbn());
        bookRepository.save(book);
    }

    @Override
    public List<BookDto> findAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map((book) -> mapToBookDto(book))
                .collect(Collectors.toList());
    }

    @Override
    public JSONObject getBookFromRemoteAPI(String isbn) {
        JSONObject emplyJSONObject = new JSONObject();
        try {
            return RemoteAPI.get(String.format("https://openlibrary.org/api/books?bibkeys=ISBN:%s&jscmd=details&format=json", isbn));
        } catch (IOException e) {
            e.printStackTrace();
            return emplyJSONObject;
        }
    }

    @Override
    public List<BookDto> getMostPopularBookDtos() {
        // Does not work :/
//        List<Book> books = bookRepository.findTop10ByUsers();

        List<Book> books = bookRepository.findAll();
        Collections.sort(books, (b1, b2) -> b2.getUsers().size() - b1.getUsers().size());
        List<Book> topTen = books.subList(0, 10);
        return topTen.stream()
                .map((book) -> mapToBookDto(book))
                .collect(Collectors.toList());
    }

    @Override
    public BookDto mapToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());

        // get the current authenticated user
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated() ||
                SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")
        ) {
            AuthUserDto authUserDto = userService.getAuthUserDto();
            // check if the book is in the user's list of books
            if (authUserDto.getBooks() != null) {
                List<BookDto> books = authUserDto.getBooks();
                books.stream().filter((b) -> b.getId().equals(book.getId()))
                        .findFirst()
                        .ifPresent((b) -> {
                                bookDto.setFavorited(true);
                        });
            }
        }

        if (book.getWriters().size() > 0)
            bookDto.setAuthor_1(book.getWriters().get(0).getFirstName() + " " + book.getWriters().get(0).getLastName());
        if (book.getWriters().size() > 1) {
            bookDto.setAuthor_2(book.getWriters().get(1).getFirstName() + " " + book.getWriters().get(1).getLastName());
        }
        if (book.getWriters().size() > 2) {
            bookDto.setAuthor_3(book.getWriters().get(2).getFirstName() + " " + book.getWriters().get(2).getLastName());
        }

        bookDto.setHearts(book.getUsers().size());

        return bookDto;
    }
}

