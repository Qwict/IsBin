package com.qwict.isbin.repository;

import com.qwict.isbin.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository  extends JpaRepository<Book, String> {
    Book findByIsbn(String isbn);


    // Does not work :/
//    List<Book> findTop10ByUsers();
}
