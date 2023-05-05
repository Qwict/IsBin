package com.qwict.isbin.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;

    private final String regex = "^(?:ISBN(?:-13)?:? )?(?=[0-9]{13}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)97[89][- ]?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]$";

    @Pattern(
            regexp = regex,
            message = "ISBN13 should be valid"
    )
    @NotEmpty(message = "ISBN13 should not be empty")
    private String isbn;

    @NotEmpty(message = "Title should not be empty")
    private String title;

    @NotEmpty(message = "The primary author should have a first name")
    private String primaryAuthorFirstName;

    @NotEmpty(message = "The primary author should have a last name")
    private String primaryAuthorLastName;


    private String secondaryAuthorFirstName;
    private String secondaryAuthorLastName;

    private String tertiaryAuthorFirstName;
    private String tertiaryAuthorLastName;


    private long hearts;
    private boolean favorited;

//    @NotEmpty(message = "Location should not be empty")
//    private List<Location> location;

    @DecimalMin(value = "0.01", message = "Price must be above 0")
    @DecimalMax(value = "99.99", message = "Price must be below 100")
    private double price;

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", primaryAuthorFirstName='" + primaryAuthorFirstName + '\'' +
                ", primaryAuthorLastName='" + primaryAuthorLastName + '\'' +
                ", secondaryAuthorFirstName='" + secondaryAuthorFirstName + '\'' +
                ", secondaryAuthorLastName='" + secondaryAuthorLastName + '\'' +
                ", tertiaryAuthorFirstName='" + tertiaryAuthorFirstName + '\'' +
                ", tertiaryAuthorLastName='" + tertiaryAuthorLastName + '\'' +
                ", hearts=" + hearts +
                ", price=" + price +
                '}';
    }
}