package com.qwict.isbin.dto;

import com.qwict.isbin.model.Location;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

// TODO: validate isbn
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long id;

    @NotEmpty(message = "ISBN13 should not be empty")
    private String isbn;

    @NotEmpty(message = "Title should not be empty")
    private String title;

    @NotEmpty(message = "Author should not be empty")
    private String author_1;

    private String author_2;

    private String author_3;

    private long hearts;
    private boolean favorited;

//    @NotEmpty(message = "Location should not be empty")
//    private List<Location> location;

    @NotNull(message = "Price should not be empty")
    private double price;

    @Override
    public String toString() {
        return "BookDto{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author_1='" + author_1 + '\'' +
                ", author_2='" + author_2 + '\'' +
                ", author_3='" + author_3 + '\'' +
                ", hearts=" + hearts +
                ", price=" + price +
                '}';
    }
}