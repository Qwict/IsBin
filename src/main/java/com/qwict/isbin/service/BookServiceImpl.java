package com.qwict.isbin.service;

import com.qwict.isbin.domein.DomeinController;
import com.qwict.isbin.dto.AuthUserDto;
import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.LocationDto;
import com.qwict.isbin.model.Author;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.Location;
import com.qwict.isbin.repository.AuthorRepository;
import com.qwict.isbin.repository.BookRepository;
import com.qwict.isbin.repository.LocationRepository;
import org.json.simple.JSONObject;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;
    private UserService userService;
    private final DomeinController domeinController = new DomeinController();
    private final AuthorRepository authorRepository;
    private final LocationRepository locationRepository;

    public BookServiceImpl(BookRepository bookRepository, UserService userService,
                           AuthorRepository authorRepository, LocationRepository locationRepository) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.authorRepository = authorRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book getById(String id) {
        // TODO: change this when using UUID
        return bookRepository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public Book findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public Book findBookById(String id) {
        // TODO: change this when using UUID
        return bookRepository.findById(Long.valueOf(id)).orElse(null);
    }

    @Override
    public void saveBook(BookDto bookDto, BindingResult result) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setIsbn(bookDto.getIsbn());

        List<Author> authors = new ArrayList<>();
        List<Location> locations = new ArrayList<>();

        try {
            book.setPrice(Double.valueOf(bookDto.getPrice()));
        } catch (NumberFormatException e) {
            result.rejectValue("price", "price", "Price must be a number");
        }

        int authorCount = 1;
        // Create authors or get them from database if they already exist
        System.out.printf("AuthorDtos: %s%n", bookDto.getAuthorDtos());
        for (AuthorDto authorDto : bookDto.getAuthorDtos()) {
            if (!authorDto.isNotBlank()) {
                if (authorDto.getFirstName().isBlank()) {
                    result.rejectValue("firstName", null,
                            "firstname should not be empty"
                    );
                    throw new IllegalArgumentException("Author firstname should not be empty");
                } if (authorDto.getLastName().isBlank()) {
                    result.rejectValue("lastName", null,
                            "lastname should not be empty"
                    );
                    throw new IllegalArgumentException("Author lastname should not be empty");
                }
            }
            Author authorFromDatabase = authorRepository.findByFirstNameAndLastName(
                    authorDto.getFirstName(), authorDto.getLastName()
            );
            if (authorFromDatabase != null) {
                authorFromDatabase.getWritten().add(book);
                authors.add(authorFromDatabase);
            } else {
                Author author = new Author();
                author.setFirstName(authorDto.getFirstName());
                author.setLastName(authorDto.getLastName());
                author.getWritten().add(book);
                authors.add(author);
            }

            authorCount++;
        }

        int locationCount = 1;
        // Create locations
        System.out.printf("LocationDtos: %s%n", bookDto.getLocationDtos());
        for (LocationDto locationDto : bookDto.getLocationDtos()) {
            if (!locationDto.isNotBlank()) {
                result.rejectValue(
                        "locationDtos", null,
                        "Location name should not be empty"
                );
                throw new IllegalArgumentException("Location name should not be empty");
            }
            Location locationFromDatabase = locationRepository.findByNameAndPlaceCode1AndPlaceCode2(
                    locationDto.getName(), locationDto.getPlaceCode1(), locationDto.getPlaceCode2()
            );

            if (
                    Math.abs(locationDto.getPlaceCode1()-locationDto.getPlaceCode2()) < 50
            ) {
                result.rejectValue(
                        "location", null,
                        "Place codes must be at least 50 apart."
                );
                throw new IllegalArgumentException("Place codes must be at least 50 apart.");
            }

            if (locationFromDatabase != null) {
                result.rejectValue(
                        String.format("location", locationCount), null,
                        "Location is already taken by another book."
                );
                throw new IllegalArgumentException("Location is already in taken by another book.");
            }

            Location location = new Location();
            location.setName(locationDto.getName());
            location.setPlaceCode1(locationDto.getPlaceCode1());
            location.setPlaceCode2(locationDto.getPlaceCode2());
            location.setBook(book);
            locations.add(location);
            locationCount++;
        }

        try {
            authorRepository.saveAll(authors);
            locationRepository.saveAll(locations);
            bookRepository.save(book);

        } catch (Exception e) {
            System.out.printf("Error: %s", e.getMessage());
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
        bookDto.setPrice(String.format("€%s", book.getPrice()));

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

        for (Author author : book.getWriters()) {
            AuthorDto authorDto = new AuthorDto();
            authorDto.setFirstName(author.getFirstName());
            authorDto.setLastName(author.getLastName());
            bookDto.getAuthorDtos().add(authorDto);
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

