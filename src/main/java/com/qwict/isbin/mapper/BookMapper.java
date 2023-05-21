package com.qwict.isbin.mapper;

import com.qwict.isbin.dto.AuthUserDto;
import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.LocationDto;
import com.qwict.isbin.model.Author;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.Location;
import com.qwict.isbin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class BookMapper {

    public static BookDto mapToBookDtoForUserService(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());

        if (book.getPrice() != null) {
            bookDto.setPrice(String.format("€%.2f", book.getPrice()));
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

    public static BookDto mapToBookDtoForBookService(Book book, UserService userService) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        if (book.getPrice() != null) {
            bookDto.setPrice(String.format("€%s", book.getPrice()));
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
}