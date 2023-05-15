package com.qwict.isbin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class AuthorDto {
    private Long id;

    @NotNull(message = "First name should not be empty")
    @NotEmpty(message = "First name should not be empty")
    @NotBlank(message = "First name should not be blank")
    private String firstName;

    @NotNull(message = "Last name should not be empty")
    @NotEmpty(message = "Last name should not be empty")
    @NotBlank(message = "Last name should not be blank")
    private String lastName;

    private List<BookDto> written;
    private int numberOfBooks;

    public AuthorDto() {
        firstName = "";
        lastName = "";
    }

    public boolean isNotBlank() {
        return this.firstName != null && !this.firstName.isBlank() && this.lastName != null && !this.lastName.isBlank();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AuthorDto)) return false;

        AuthorDto authorDto = (AuthorDto) o;
        if (authorDto.getId().equals(this.getId())) {
            return true;
        } else if (authorDto.getFirstName().equals(this.getFirstName()) && authorDto.getLastName().equals(this.getLastName())) {
            return true;
        }

        return false;

    }
}
