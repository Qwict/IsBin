package com.qwict.isbin.service;

import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.model.Author;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.repository.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final BookService bookService;

    public AuthorServiceImpl(AuthorRepository authorRepository, BookService bookService) {
        this.authorRepository = authorRepository;
        this.bookService = bookService;
    }

    @Override
    public Author getById(String id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    public List<AuthorDto> searchAuthorsBySearchTerm(String searchTerm) {
        String firsName = "";
        String lastName = "";
        List<AuthorDto> authorMatchedDtos = new ArrayList<>();
        if (searchTerm.split(" ").length > 1) {
            firsName = searchTerm.split(" ")[0];
            lastName = searchTerm.split(" ")[1];
        } else {
            firsName = searchTerm;
            lastName = searchTerm;
        }

        // Best match
        Author authorBestMatch = authorRepository.findByFirstNameAndLastName(firsName, lastName);
        if (authorBestMatch != null) {
            AuthorDto authorBestMatchDto = mapToAuthorDto(authorBestMatch);
            authorMatchedDtos.add(authorBestMatchDto);
        }

        // Other matches
        List<Author> authorsMatchLastName;
        List<Author> authorsMatchFirstName;

        // Match by last name
        authorsMatchLastName = authorRepository.findByLastName(lastName);
        authorsMatchLastName.forEach(author -> {
            if (author != null) {
                AuthorDto authorDto = mapToAuthorDto(author);
                System.out.printf("Added author %s %s to authorMatchedDtos%n", author.getFirstName(), author.getLastName());
                if (!authorMatchedDtos.contains(authorDto)) {
                    authorMatchedDtos.add(authorDto);
                }
            }
        });

        // Match by first name
        authorsMatchFirstName = authorRepository.findByFirstName(firsName);
        authorsMatchFirstName.forEach(author -> {
            if (author != null) {
                AuthorDto authorDto = mapToAuthorDto(author);
                System.out.printf("Added author %s %s to authorMatchedDtos%n", author.getFirstName(), author.getLastName());
                if (!authorMatchedDtos.contains(authorDto)) {
                    authorMatchedDtos.add(authorDto);
                }
            }
        });

        return authorMatchedDtos;
    }

    @Override
    public AuthorDto getAuthorDtoById(String id) {
        Author author = getById(id);
        return mapToAuthorDto(author);
    }

    @Override
    public AuthorDto mapToAuthorDto(Author author) {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(author.getId());
        authorDto.setFirstName(author.getFirstName());
        authorDto.setLastName(author.getLastName());
        List<Book> written = author.getWritten();
        List<BookDto> writtenDtos = new ArrayList<>();
        written.forEach(book -> {
            BookDto bookDto = bookService.mapToBookDto(book);
            writtenDtos.add(bookDto);
        });
        authorDto.setNumberOfBooks(written.size());
        authorDto.setWritten(writtenDtos);
        return authorDto;
    }

}
