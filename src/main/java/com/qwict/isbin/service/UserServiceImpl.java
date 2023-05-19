package com.qwict.isbin.service;

import com.qwict.isbin.dto.*;
import com.qwict.isbin.model.*;
import com.qwict.isbin.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public void deleteUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found in database"));
        if (user.getRoles().contains(roleService.findRoleByName("ROLE_OWNER"))) {
            throw new IllegalArgumentException("Cannot delete owner");
        }
        userRepository.deleteById(id);
    }

    @Override
    public void updateUserWithChangeUserDto(ChangeUserDto updatedUser) {
        System.out.printf("INFO -- UserServiceImpl -- updateUserWithChangeUserDto\n\tupdatedUser: %s with id: %d\n", updatedUser, updatedUser.getId());
        User user = userRepository.findById(updatedUser.getId()).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("User not found in database");
        }
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        user.setMaxFavorites(updatedUser.getMaxFavorites());

        if (!updatedUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        Role roleUser = roleService.findRoleByName("ROLE_USER");
        Role roleAdmin = roleService.findRoleByName("ROLE_ADMIN");
        Role roleOwner = roleService.findRoleByName("ROLE_OWNER");
        user.getRoles().clear();
        if (updatedUser.getUpdateToRole() == 0)
            user.getRoles().add(roleUser);
        if (updatedUser.getUpdateToRole() == 1)
            user.getRoles().addAll(Arrays.asList(roleUser, roleAdmin));
        if (updatedUser.getUpdateToRole() == 2)
            user.getRoles().addAll(Arrays.asList(roleUser, roleAdmin, roleOwner));
        userRepository.save(user);
    }


    // causes circular dependency if taken from UserServiceImpl
    public BookDto mapToBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        bookDto.setIsbn(book.getIsbn());
        bookDto.setPrice(String.format("€%s", book.getPrice()));

        for (Author author : book.getWriters()) {
            AuthorDto authorDto = new AuthorDto();
            authorDto.setFirstName(author.getFirstName());
            authorDto.setLastName(author.getLastName());
            if (bookDto.getAuthorDtos() == null) {
                bookDto.setAuthorDtos(new ArrayList<>());
            }
            bookDto.getAuthorDtos().add(authorDto);
        }

        for (Location location : book.getLocations()) {
            LocationDto locationDto = new LocationDto();
            locationDto.setName(location.getName());
            locationDto.setPlaceCode1(location.getPlaceCode1());
            locationDto.setPlaceCode2(location.getPlaceCode2());
            if (bookDto.getLocationDtos() == null) {
                bookDto.setLocationDtos(new ArrayList<>());
            }
            bookDto.getLocationDtos().add(locationDto);
        }

        bookDto.setHearts(book.getUsers().size());
        return bookDto;
    }

    @Override
    public List<ChangeUserDto> getAllChangeUserDtos() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToChangeUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChangeUserDto getChangeUserDtoById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        return mapToChangeUserDto(user);
    }

    public ChangeUserDto mapToChangeUserDto(User user) {
        ChangeUserDto changeUserDto = new ChangeUserDto();
        changeUserDto.setId(user.getId());
        changeUserDto.setUsername(user.getUsername());
        changeUserDto.setEmail(user.getEmail());
        changeUserDto.setMaxFavorites(user.getMaxFavorites());
        changeUserDto.setRoles(user.getRoles().stream()
                .map((role) -> roleService.mapToRoleDto(role))
                .collect(Collectors.toList())
        );
        changeUserDto.setFavoritedBooksCount(user.getBooks().size());
        return changeUserDto;
    }
}
