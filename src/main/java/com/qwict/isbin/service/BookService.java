package com.qwict.isbin.service;

import com.qwict.isbin.domein.RemoteAPI;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.repository.BookRepository;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    public Book getById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    public JSONObject getBookFromRemoteAPI(String isbn) {
        JSONObject emplyJSONObject = new JSONObject();
        try {
            return RemoteAPI.get(String.format("https://openlibrary.org/api/books?bibkeys=ISBN:%s&jscmd=details&format=json", isbn));
        } catch (IOException e) {
            e.printStackTrace();
            return emplyJSONObject;
        }
    }
}
