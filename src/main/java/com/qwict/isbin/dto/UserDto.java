package com.qwict.isbin.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotEmpty(message = "{usernameNotEmpty}")
    private String username;
    @NotEmpty(message = "{emailNotEmpty}")
    @Email
    private String email;
    @NotEmpty(message = "{passwordNotEmpty}")
    private String password;
}