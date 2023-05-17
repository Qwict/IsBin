package com.qwict.isbin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class ChangeUserDto {
    private long id;
    @NotEmpty(message = "{usernameNotEmpty}")
    private String username;
    @NotEmpty(message = "{emailNotEmpty}")
    @Email
    private String email;
    @NotEmpty(message = "{passwordNotEmpty}")
    private String password;
    private int maxFavorites;
    private int favoritedBooksCount;
    private List<RoleDto> roles;
    private int updateToRole;
}
