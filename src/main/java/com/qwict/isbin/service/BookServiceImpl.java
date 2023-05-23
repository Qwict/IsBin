package com.qwict.isbin.service;

import com.qwict.isbin.domein.DomeinController;
import com.qwict.isbin.domein.Formatter;
import com.qwict.isbin.dto.AuthUserDto;
import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.LocationDto;
import com.qwict.isbin.model.Author;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.Location;
import com.qwict.isbin.model.User;
import com.qwict.isbin.repository.AuthorRepository;
import com.qwict.isbin.repository.BookRepository;
import com.qwict.isbin.repository.LocationRepository;
import com.qwict.isbin.repository.UserRepository;
import org.hibernate.cfg.NotYetImplementedException;
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
    private final UserRepository userRepository;

    public BookServiceImpl(BookRepository bookRepository, UserService userService,
                           AuthorRepository authorRepository, LocationRepository locationRepository,
                           UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.authorRepository = authorRepository;
        this.locationRepository = locationRepository;
        this.userRepository = userRepository;
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
    public void saveBook(BookDto bookDto, BindingResult result, boolean isEdit) {
        Book book = new Book();
        book.setTitle(bookDto.getTitle());
        book.setIsbn(Formatter.formatISBNToString(bookDto.getIsbn()));

        double price;
        bookDto.getPrice().replace("$", "");
        bookDto.getPrice().replace(",", ".");
        bookDto.getPrice().replace(" ", "");
        bookDto.getPrice().replace("€", "");
        try {
            price = Double.parseDouble(bookDto.getPrice());
            double priceTwoDecimals = Math.round(price * 100.0) / 100.0;
            if (priceTwoDecimals > 0.01 && priceTwoDecimals < 99.99) {
                book.setPrice(priceTwoDecimals);
            } else {
                book.setPrice(null);
            }
        } catch (NumberFormatException e) {
            book.setPrice(null);
        }

        bookRepository.save(book);
        List<AuthorDto> authorDtos = bookDto.getAuthorDtos();
        for (AuthorDto authorDto : authorDtos) {
            if (!authorDto.getFirstName().isEmpty() && !authorDto.getLastName().isEmpty()) {
//                System.out.printf("Looking in database for author: %s %s\n", authorDto.getFirstName(), authorDto.getLastName());
                Author authorFromDatabase = authorRepository.findByFirstNameAndLastName(authorDto.getFirstName(), authorDto.getLastName());

                if (authorFromDatabase != null) {
//                    System.out.printf("Found author in database: %s %s\n", authorFromDatabase.getFirstName(), authorFromDatabase.getLastName());
                    addBookToAuthor(authorFromDatabase, book);
                } else {
//                    System.out.printf("Author not found in database: %s %s\n", authorDto.getFirstName(), authorDto.getLastName());
                    Author newAuthor = new Author();
                    newAuthor.setFirstName(authorDto.getFirstName());
                    newAuthor.setLastName(authorDto.getLastName());
                    authorRepository.save(newAuthor);
                    addBookToAuthor(newAuthor, book);
                }
            }
        }

        List<LocationDto> locationDtos = bookDto.getLocationDtos();
        for (LocationDto locationDto : locationDtos) {
            if (locationDto.getPlaceCode1() != 0 && locationDto.getPlaceCode2() != 0 && !locationDto.getName().isEmpty()) {
                Location locationFromDatabase = locationRepository.findByNameAndPlaceCode1AndPlaceCode2(locationDto.getName(), locationDto.getPlaceCode1(), locationDto.getPlaceCode2());
                if (locationFromDatabase == null) {
                    Location newLocation = new Location();
                    newLocation.setName(locationDto.getName());
                    newLocation.setPlaceCode1(locationDto.getPlaceCode1());
                    newLocation.setPlaceCode2(locationDto.getPlaceCode2());
                    newLocation.setBook(book);
                    locationRepository.save(newLocation);
                } else {
                    if (locationFromDatabase.getBook() != null) {
                        System.out.printf("WARNING -- BookServiceImpl -- saveBook\n\tLocation already has a book: %s\n", locationFromDatabase.getName());
                    } else {
//                        System.out.printf("Location has no book yet: %s\n", locationFromDatabase.getName());
                        locationFromDatabase.setBook(book);
                        locationRepository.save(locationFromDatabase);
                    }
                }
            }
        }
//        throw new NotYetImplementedException("this method is not yet implemented");
        System.out.printf("INFO -- BookServiceImpl -- saveBook\n\tBook saved: %s\n", book.getTitle());
    }

    @Override
    public void deleteBook(String id) {
        Book bookToDelte = bookRepository.findById(Long.valueOf(id)).orElse(null);

        if (bookToDelte == null) {
            System.out.printf("ERROR -- BookServiceImpl -- deleteBook()\n\tBook to delete not found: %s\n", id);
            throw new IllegalArgumentException("Book not found");
        }

        List<Location> locationsToRemove = new ArrayList<>(bookToDelte.getLocations());
        for (Location location : locationsToRemove) {
            bookToDelte.getLocations().remove(location);
            bookRepository.save(bookToDelte);
        }

        // TODO: this is very bad, imagine 200.000 likes on a book, this will take forever
        for (User user : bookToDelte.getUsers()) {
            user.getBooks().remove(bookToDelte);
            userRepository.save(user);
        }

        for (Author author : bookToDelte.getWriters()) {
            author.getWritten().remove(bookToDelte);
            authorRepository.save(author);
        }

        bookRepository.delete(bookToDelte);
        System.out.printf("INFO -- BookServiceImpl -- deleteBook()\n\tBook deleted: %s\n", bookToDelte.getTitle());
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
        List<Book> topTen = new ArrayList<>();
        try {
            topTen = books.subList(0, 10);
        } catch (IndexOutOfBoundsException e) {
            topTen = books.subList(0, books.size());
        }
        return topTen.stream()
                .map((book) -> mapToBookDto(book))
                .collect(Collectors.toList());
    }

    // TODO: add this code to the mapper to avoid code duplication
    @Override
    public BookDto mapToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        if (book.getPrice() != null) {
            bookDto.setPrice(String.format("€%.2f", book.getPrice()));
        }

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
            if (bookDto.getAuthorDtos() == null) {
                bookDto.setAuthorDtos(new ArrayList<>());
            }
            bookDto.getAuthorDtos().add(authorDto);
        }

        for (Location location : book.getLocations()) {
            LocationDto locationDto = new LocationDto();
            locationDto.setName(location.getName());
            locationDto.setPlaceCode1(location.getPlaceCode1());
            locationDto.setPlaceCode2(location.getPlaceCode2());
            if (bookDto.getLocationDtos() == null) {
                bookDto.setLocationDtos(new ArrayList<>());
            }
            bookDto.getLocationDtos().add(locationDto);
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

    private void addBookToAuthor(Author author, Book book) {
        if (author.getWritten() == null) {
            author.setWritten(new ArrayList<>());
            author.getWritten().add(book);
        } else {
            List<Book> written = author.getWritten();
            written.add(book);
            author.setWritten(written);
        }
        authorRepository.save(author);
    }
}

