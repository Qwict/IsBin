package com.qwict.isbin.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class BookDto {
    private Long id;
//    private final String regex = "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$";

    @Pattern(
            regexp = "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$",
            message = "ISBN13 should be valid"
    )
    @NotEmpty(message = "ISBN13 should not be empty")
    private String isbn;

    @NotEmpty(message = "Title should not be empty")
    private String title;

//    @NotEmpty(message = "Author 1 should not be empty")
//    private AuthorDto primaryAuthor;
//    @NotEmpty(message = "Location 1 should not be empty")
//    private LocationDto primaryLocation;

//    @NotEmpty(message = "Author 1 should not be empty")
    @NotEmpty(message = "Author 1 should not be empty")
    private ArrayList<AuthorDto> authorDtos = new ArrayList<>();

//    @NotEmpty(message = "Location 1 should not be empty")
    @NotEmpty(message = "Location 1 should not be empty")
    private ArrayList<LocationDto> locationDtos = new ArrayList<>();

    private long hearts;
    private boolean favorited;

    @DecimalMin(value = "0.01", message = "Price must be above 0")
    @DecimalMax(value = "99.99", message = "Price must be below 100")
    private String price;
}