package com.qwict.isbin.model;

import com.qwict.isbin.repository.RoleRepository;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;

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

    // User is the owning side of roles (a user has many roles)
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")}
    )
    private List<Role> roles = new ArrayList<>();

// User is the owning side of books (a user has many books in his favorites)
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_books",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="BOOK_ID", referencedColumnName="ID")}
    )
    public List<Book> books = new ArrayList<>();
//    public Set<Book> favoritedBooks;

    public User(String email, String username, Integer maxFavorites, String password) {
        this.email = email;
        this.username = username;
        this.maxFavorites = maxFavorites;
        this.password = password;
    }

    public User(String email, String username, Integer maxFavorites, String password, List<Role> roles) {
        this.email = email;
        this.username = username;
        this.maxFavorites = maxFavorites;
        this.password = password;
        this.roles = roles;
    }

    public void addBookToFavorites(Book book) {
        System.out.printf("User.addBookToFavorites: book=%s%n", book);
        this.books.add(book);
        book.getUsers().add(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email +
                ", username='" + username +
                ", maxFavorites=" + maxFavorites +
                ", password='" + password +
                ", roles=" + roles +
                ", books=" + books +
                '}';
    }
}
