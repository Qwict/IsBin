package com.qwict.isbin.service;

import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.Role;
import com.qwict.isbin.model.User;
import org.json.simple.JSONObject;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface BookService {
    Book findBookById(String id);

    void saveBook(BookDto bookDto, BindingResult result, boolean isEdit);

    void deleteBook(String id);
    Book findBookByIsbn(String isbn);
    List<BookDto> findAllBooks();

    List<Book> findAll();

    Book getById(String id);

    JSONObject getBookFromRemoteAPI(String isbn);
    BookDto mapToBookDto(Book book);

    List<BookDto> getMostPopularBookDtos();

    List<BookDto> searchBooksByAuthorDtos(List<AuthorDto> authorDtos);

}
