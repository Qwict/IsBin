package com.qwict.isbin.service;

import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.Role;
import com.qwict.isbin.model.User;
import org.json.simple.JSONObject;

import java.util.List;

public interface BookService {
    Book findBookById(String id);

    void saveBook(BookDto bookDto);

    Book findBookByIsbn(String isbn);
    List<BookDto> findAllBooks();

    List<Book> findAll();

    Book getById(String id);

    JSONObject getBookFromRemoteAPI(String isbn);
    BookDto mapToBookDto(Book book);

    List<BookDto> getMostPopularBookDtos();
}
