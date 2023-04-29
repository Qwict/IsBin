package com.qwict.isbin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// TODO: validate isbn
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto
{
    private Long id;
    @NotEmpty
    private String isbn;

    @NotEmpty
    private String title;

    @NotEmpty
    private String author_1;

    private String author_2;

    private String author_3;

    @NotEmpty
    private double price;
}