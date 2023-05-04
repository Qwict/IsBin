package com.qwict.isbin.service;

import com.qwict.isbin.domein.DomeinController;
import com.qwict.isbin.domein.RemoteAPI;
import com.qwict.isbin.dto.AuthUserDto;
import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.model.Author;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.repository.AuthorRepository;
import com.qwict.isbin.repository.BookRepository;
import org.json.simple.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;
    private UserService userService;
    private final DomeinController domeinController = new DomeinController();
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, UserService userService,
                           AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.authorRepository = authorRepository;
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
        book.setPrice(bookDto.getPrice());


        String[][] authors = {
                {bookDto.getPrimaryAuthorFirstName(), bookDto.getPrimaryAuthorLastName()},
                {bookDto.getSecondaryAuthorFirstName(), bookDto.getSecondaryAuthorLastName()},
                {bookDto.getTertiaryAuthorFirstName(), bookDto.getTertiaryAuthorLastName()}
        };

        for (String[] author : authors) {
            // author must have a first and last name
            if(!author[0].isBlank() && !author[1].isBlank() ) {
                bookRepository.save(book);
                Book bookFromDatabase = bookRepository.findByIsbn(book.getIsbn());

                Author authorFromDatabase = authorRepository.findByFirstNameAndLastName(author[0], author[1]);
                if (authorFromDatabase == null) {
                    System.out.printf("Creating new author: %s %s\n", author[0], author[1]);
                    Author newAuthor = new Author();
                    newAuthor.setFirstName(author[0]);
                    newAuthor.setLastName(author[1]);
                    newAuthor.setWritten(List.of(bookFromDatabase));
                    authorRepository.save(newAuthor);
                } else {
                    System.out.printf("Author already exists: %s %s\n", author[0], author[1]);
                    if (authorFromDatabase.getWritten() == null) {
                        authorFromDatabase.setWritten(new ArrayList<>());
                    }
                    authorFromDatabase.getWritten().add(book);
                    authorRepository.save(authorFromDatabase);
                }
            }
        }
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
            return domeinController.get(String.format("https://openlibrary.org/api/books?bibkeys=ISBN:%s&jscmd=details&format=json", isbn));
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

        if (book.getWriters().size() > 0) {
            bookDto.setPrimaryAuthorFirstName(book.getWriters().get(0).getFirstName());
            bookDto.setPrimaryAuthorLastName(book.getWriters().get(0).getLastName());
        } if (book.getWriters().size() > 1) {
            bookDto.setSecondaryAuthorFirstName(book.getWriters().get(1).getFirstName());
            bookDto.setSecondaryAuthorLastName(book.getWriters().get(1).getLastName());
        } if (book.getWriters().size() > 2) {
            bookDto.setTertiaryAuthorFirstName(book.getWriters().get(2).getFirstName());
            bookDto.setTertiaryAuthorLastName(book.getWriters().get(2).getLastName());
        }

        bookDto.setHearts(book.getUsers().size());
        return bookDto;
    }

    @Override
    public List<BookDto> searchBooksByAuthorDtos(List<AuthorDto> authorDtos) {
        List<Book> books = new ArrayList<>();
        for (AuthorDto authorDto : authorDtos) {
            Author author = authorRepository.findByFirstNameAndLastName(authorDto.getFirstName(), authorDto.getLastName());
            books.addAll(bookRepository.findByWriters_id(author.getId()));
        }

        return books.stream()
                .map(this::mapToBookDto)
                .collect(Collectors.toList());
    }
}

