package com.qwict.isbin.repository;

import com.qwict.isbin.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository  extends JpaRepository<Author, Long> {
    Author findByFirstNameAndLastName(String firstName, String lastName);

    List<Author> findByFirstName(String firstName);
    List<Author> findByLastName(String lastName);

}
