package com.qwict.isbin.service;

import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.model.Author;

import java.util.List;

public interface AuthorService {
    List<AuthorDto> searchAuthorsBySearchTerm(String searchTerm);

    AuthorDto mapToAuthorDto(Author author);
    Author getById(String id);
    AuthorDto getAuthorDtoById(String id);

    AuthorDto getByFirstNameAndLastName(String firstName, String lastName);
}
