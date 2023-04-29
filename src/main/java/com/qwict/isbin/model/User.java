package com.qwict.isbin.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private String id;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String email;

    @Column(nullable=false)
    private String username;

    @Column(name="max_favorites", columnDefinition = "integer default 10")
    private Integer maxFavorites;

    @Column(nullable=false)
    private String password;

//    @Column(name="hash")
//    private String hash;
//
//    @Column(name="salt")
//    private String salt;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")})
    private List<Role> roles = new ArrayList<>();

// User is the owning side of books (a user has many books in his favorites
    @ManyToMany
    @JoinTable(name="users_books")
    public Set<Book> favoritedBooks;

    public User(String email, String username, Integer maxFavorites, String password) {
        this.email = email;
        this.username = username;
        this.maxFavorites = maxFavorites;
        this.password = password;
    }

//    public User(
//            String email,
//            String username,
//            Set<Role> role,
//            Integer maxFavorites,
//            String hash,
//            String salt
//    ) {
//        setEmail(email);
//        setUsername(username);
//        setRoles(role);
//        setMaxFavorites(maxFavorites);
//        setHash(hash);
//        setSalt(salt);
//    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email +
                ", username='" + username +
                ", maxFavorites=" + maxFavorites +
                ", password='" + password +
                ", roles=" + roles +
                ", favoritedBooks=" + favoritedBooks +
                '}';
    }
}
