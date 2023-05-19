package com.qwict.isbin.controller;

import com.qwict.isbin.dto.AuthUserDto;
import com.qwict.isbin.dto.BookDto;
import com.qwict.isbin.dto.RoleDto;
import com.qwict.isbin.dto.SearchDto;
import com.qwict.isbin.model.User;
import com.qwict.isbin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalControllerAdvice {
    @Autowired
    private UserService userService;

//    // TODO: why does this not work!
//    @ModelAttribute("authenticatedUser")
//    public AuthenticatedUserDto populateUser() {
//        AuthenticatedUserDto authenticatedUserDto = getAuthUser();
//        return authenticatedUserDto;
//    }

    @ModelAttribute("authUsername")
    public String populateUsername() {
        AuthUserDto authUser = getAuthUser();
        return authUser.getUsername();
    }

    @ModelAttribute("authEmail")
    public String populateEmail() {
        AuthUserDto authUser = getAuthUser();
        return authUser.getEmail();
    }

    @ModelAttribute("authMaxFavorites")
    public Integer populateMaxFavorites() {
        AuthUserDto authUser = getAuthUser();
        return authUser.getMaxFavorites();
    }

    @ModelAttribute("authFavoritesBooks")
    public Integer populateFavoritedBooksCount() {
        AuthUserDto authUser = getAuthUser();
        return authUser.getFavoritedBooksCount();
    }

    @ModelAttribute("authBooks")
    public List<BookDto> populateBooks() {
        AuthUserDto authUser = getAuthUser();
        return authUser.getBooks();
    }

    @ModelAttribute("authRoles")
    public List<RoleDto> populateRoles() {
        AuthUserDto authUser = getAuthUser();
        return authUser.getRoles();
    }

    @ModelAttribute("reachedMaxFavorites")
    public boolean populateReachedMaxFavorites() {
//        return true;
        AuthUserDto authUser = getAuthUser();
        if (authUser.getMaxFavorites() == null) {
            return true;
        }
        return authUser.getMaxFavorites() <= authUser.getFavoritedBooksCount();
    }

    @ModelAttribute("searchDto")
    public SearchDto populateSearchObject() {
        return new SearchDto();
    }

    private AuthUserDto getAuthUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!auth.isAuthenticated() || auth.getName().equals("anonymousUser")) {
            AuthUserDto anon = new AuthUserDto();
            anon.setUsername(auth.getName());
            anon.setMaxFavorites(0);
            anon.setFavoritedBooksCount(0);
            return anon;
        }
        User user = userService.findUserByEmail(auth.getName());
        return userService.mapToAuthenticatedUserDto(user);
    }
}