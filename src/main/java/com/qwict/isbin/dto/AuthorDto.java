package com.qwict.isbin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {
    private Long id;
    private String firstName;
    private String lastName;

    private List<BookDto> written;
    private int numberOfBooks;

    @Override
    public String toString() {
        return "AuthorDto{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", written=" + written +
                ", numberOfBooks=" + numberOfBooks +
                '}';
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
