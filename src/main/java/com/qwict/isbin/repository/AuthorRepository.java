package com.qwict.isbin.repository;

import com.qwict.isbin.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository  extends JpaRepository<Author, String> {
}
