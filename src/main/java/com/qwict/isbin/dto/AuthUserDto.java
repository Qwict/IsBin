package com.qwict.isbin.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AuthUserDto {
    private String username;
    private String email;
    private Integer maxFavorites;
    private Integer favoritedBooksCount;
    private List<BookDto> books;
    private List<RoleDto> roles;
}
