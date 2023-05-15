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
public class AuthUserDto {
    private String username;
    private String email;
    private Integer maxFavorites;
    private Integer favoritedBooksCount;
    private List<BookDto> books;
    private List<RoleDto> roles;

    @Override
    public String toString() {
        return "%n%tAuthUserDto{" +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", maxFavorites=" + maxFavorites +
                ", favoritedBooksCount=" + favoritedBooksCount +
                ", books=" + books +
                ", roles=" + roles +
                '}';
    }
}
