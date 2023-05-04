package com.qwict.isbin.service;

import com.qwict.isbin.dto.AuthUserDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.UserDto;
import com.qwict.isbin.model.Book;
import com.qwict.isbin.model.Role;
import com.qwict.isbin.model.User;
import com.qwict.isbin.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;


    public UserServiceImpl(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        // encrypt the password using spring security
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role roleUser = roleService.findRoleByName("ROLE_USER");
        if(roleUser == null){
            roleUser = checkRoleExist("ROLE_USER");
        }
        user.setMaxFavorites(10);
        user.setRoles(Arrays.asList(roleUser));
        userRepository.save(user);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map((user) -> mapToUserDto(user))
                .collect(Collectors.toList());
    }

    @Override
    public UserDto mapToUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    @Override
    public AuthUserDto mapToAuthenticatedUserDto(User user) {
        AuthUserDto authenticatedUserDto = new AuthUserDto();
        authenticatedUserDto.setUsername(user.getUsername());
        authenticatedUserDto.setEmail(user.getEmail());
        authenticatedUserDto.setRoles(user.getRoles().stream()
                .map((role) -> roleService.mapToRoleDto(role))
                .collect(Collectors.toList())
        );
        authenticatedUserDto.setBooks(user.getBooks().stream()
                .map((book) -> mapToBookDto(book))
                .collect(Collectors.toList())
        );

        authenticatedUserDto.setMaxFavorites(user.getMaxFavorites());
        authenticatedUserDto.setFavoritedBooksCount(user.getBooks().size());

        return authenticatedUserDto;
    }

    private Role checkRoleExist(String roleName){
        Role role = new Role();
        role.setName(roleName);
        return roleService.saveRole(role);
    }


    @Override
    public AuthUserDto getAuthUserDto() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            AuthUserDto anon = new AuthUserDto();
            anon.setUsername(auth.getName());
            return anon;
        }
        User user = findUserByEmail(auth.getName());
        return mapToAuthenticatedUserDto(user);
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }


    // causes cirtular dependency if taken from UserServiceImpl
    public BookDto mapToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(book.getPrice());


        if (book.getWriters().size() > 0) {
            bookDto.setPrimaryAuthorFirstName(book.getWriters().get(0).getFirstName());
            bookDto.setPrimaryAuthorLastName(book.getWriters().get(0).getLastName());
        } if (book.getWriters().size() > 1) {
            bookDto.setSecondaryAuthorFirstName(book.getWriters().get(1).getFirstName());
            bookDto.setSecondaryAuthorLastName(book.getWriters().get(1).getLastName());
        } if (book.getWriters().size() > 2) {
            bookDto.setTertiaryAuthorFirstName(book.getWriters().get(2).getFirstName());
            bookDto.setTertiaryAuthorLastName(book.getWriters().get(2).getLastName());
        }

        bookDto.setHearts(book.getUsers().size());
        return bookDto;
    }
}
