package com.qwict.isbin.repository;

import com.qwict.isbin.model.Author;
import com.qwict.isbin.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository  extends JpaRepository<Book, Long> {
    Book findByIsbn(String isbn);

//    List<Book> findBookByWriters(List<Author> authors);
    List<Book> findByWriters_firstNameAndWriters_lastName(String firstName, String lastName);
    List<Book> findByWriters_id(Long id);
        //    List<Test> findByUsers_UserName(String userName)

    // Does not work :/
//    List<Book> findTop10ByUsers();
}
