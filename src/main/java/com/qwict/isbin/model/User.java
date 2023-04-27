package com.qwict.isbin.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name="email")
    private String email;
    @Column(name="username")
    private String username;

    @Column(name="role")
    private String role;

    @Column(name="max_favorites")
    private Integer maxFavorites;

    @Column(name="hash")
    private String hash;

    @Column(name="salt")
    private String salt;

// User is the owning side of books (a user has many books in his favorites
    @ManyToMany
    @JoinTable(name="users_books")
    public Set<Book> favoritedBooks;

    public User(
            String email,
            String username,
            String role,
            Integer maxFavorites,
            String hash,
            String salt
    ) {
        setEmail(email);
        setUsername(username);
        setRole(role);
        setMaxFavorites(maxFavorites);
        setHash(hash);
        setSalt(salt);

    }

    public User() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getMaxFavorites() {
        return maxFavorites;
    }

    public void setMaxFavorites(Integer maxFavorites) {
        this.maxFavorites = maxFavorites;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public Set<Book> getFavoritedBooks() {
        return favoritedBooks;
    }

    public void setFavoritedBooks(Set<Book> favoritedBooks) {
        this.favoritedBooks = favoritedBooks;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email +
                ", username='" + username +
                ", role='" + role +
                ", maxFavorites='" + maxFavorites +
//                ", hash='" + hash +
//                ", salt='" + salt +
//                ", favoritedBooks='" + favoritedBooks +
                '}';
    }
}
