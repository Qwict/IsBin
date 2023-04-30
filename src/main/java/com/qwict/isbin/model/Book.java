package com.qwict.isbin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "books", uniqueConstraints = { @UniqueConstraint(columnNames = { "isbn" }) })
//@Table(name="book")
public class Book {
//  ----------------- Fields -----------------
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private String id;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="isbn")
    private String isbn;

    @Column(name="title")
    private String title;


    @Column(name="price")
    private double price;


    // Book is the owning side of locations (a book has many locations)
    @OneToMany(cascade=ALL, mappedBy="book")
    private Set<Location> locations;

    // Book is the inverse side of authors (a book is owned by an author)
    @ManyToMany(mappedBy= "written")
    private List<Author> writers;
//    private Set<Author> writtenBy;


    // Book is the inverse side of favoritedBy (a book is favorited by a user)
//    @ManyToMany(mappedBy="favoritedBooks")
//    private Set<User> favoritedBy;

    @ManyToMany(mappedBy="books")
    private List<User> users;

    public Book(String isbn, String title, Double price, List<Author> writers, List<User> users) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.writers = writers;
        this.users = users;
    }

    public Book(String isbn, String title, Double price, List<Author> writers) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.writers = writers;
    }

    public Book(String isbn, String title, Double price) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", locations=" + locations +
                ", writers=" + writers +
                ", users=" + users +
                "}\n";
    }
}
