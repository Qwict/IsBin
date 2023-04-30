package com.qwict.isbin.service;

import com.qwict.isbin.domein.RemoteAPI;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.repository.BookRepository;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
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
    public BookDto mapToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        return bookDto;
    }
}

