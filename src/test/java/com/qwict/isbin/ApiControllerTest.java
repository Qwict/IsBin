package com.qwict.isbin;

import java.util.List;

import com.qwict.isbin.controller.ApiController;
import com.qwict.isbin.dto.AuthorDto;
import com.qwict.isbin.model.Author;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.repository.AuthorRepository;
import com.qwict.isbin.service.AuthorService;
import com.qwict.isbin.service.BookService;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;


@SpringBootTest
public class ApiControllerTest {
    @Mock
    private BookService bookServiceMock;
    @Mock
    private AuthorService authorServiceMock;
    @Mock
    private AuthorRepository authorRepositoryMock;
    private ApiController apiController;
    private MockMvc mockMvc;

    @Test
    public void test_GetAuthor() throws Exception {
        MockitoAnnotations.openMocks(this);
        apiController = new ApiController();
        mockMvc = standaloneSetup(apiController).build();
        ReflectionTestUtils.setField(apiController, "bookService", bookServiceMock);
        ReflectionTestUtils.setField(apiController, "authorService", authorServiceMock);
        ReflectionTestUtils.setField(apiController, "authorRepository", authorRepositoryMock);

        Book book = new Book("9780201633610", "Design Patterns", 28.99);
        Mockito.when(bookServiceMock.findBookByIsbn("9780201633610")).thenReturn(book);
        bookServiceMock.mapToBookDto(book);

        Author author1 = new Author("Erich", "Gamma");
        author1.setWritten(List.of(book));
        Author author2 = new Author("Richard", "Helm");
        author2.setWritten(List.of(book));
        Author author3 = new Author("Ralph", "Johnson");
        author3.setWritten(List.of(book));
        Mockito.when(authorRepositoryMock.saveAll(List.of(author1, author2, author3))).thenReturn(List.of(author1, author2, author3));

        AuthorDto author1Dto = authorServiceMock.mapToAuthorDto(author1);
        Mockito.when(authorServiceMock.getByFirstNameAndLastName("Erich", "Gamma")).thenReturn(author1Dto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/public/author/Erich/Gamma")).andExpect(status().isOk());

        Mockito.verify(authorServiceMock).getByFirstNameAndLastName("Erich", "Gamma");
    }

    @Test
    public void test_GetBook() throws Exception {
        MockitoAnnotations.openMocks(this);
        apiController = new ApiController();
        mockMvc = standaloneSetup(apiController).build();
        ReflectionTestUtils.setField(apiController, "bookService", bookServiceMock);
        Book book = new Book("9780201633610", "Design Patterns", 28.99);
        Mockito.when(bookServiceMock.findBookByIsbn("9780201633610")).thenReturn(book);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/public/book/9780201633610")).andExpect(status().isOk());
        Mockito.verify(bookServiceMock).findBookByIsbn("9780201633610");
        mockMvc.perform(MockMvcRequestBuilders.get("/api/public/book/9780201633610"))
                .andExpect(status().isOk());
    }

}

