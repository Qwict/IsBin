package com.qwict.isbin.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class BookDto {
    private Long id;
    private String isbn;

    @NotEmpty(message = "Title should not be empty")
    private String title;

    private List<AuthorDto> authorDtos;

    private List<LocationDto> locationDtos;

    private double validatedPrice;

    private long hearts;

    private boolean favorited;

    private String price;
}