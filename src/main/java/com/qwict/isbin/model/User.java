package com.qwict.isbin.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
