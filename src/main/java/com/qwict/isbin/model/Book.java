package com.qwict.isbin.model;

import jakarta.persistence.*;

import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "books", uniqueConstraints = { @UniqueConstraint(columnNames = { "isbn" }) })
//@Table(name="book")
public class Book {
//  ----------------- Fields -----------------
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

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
    @ManyToMany(mappedBy= "books")
    private Set<Author> writtenBy;


    // Book is the inverse side of favoritedBy (a book is favorited by a user)
    @ManyToMany(mappedBy="favoritedBooks")
    private Set<User> favoritedBy;

// ----------------- Constructors -----------------

    public Book() {}

    public Book(
            String isbn,
            String title,
            Double price,
            Set<Location> locations,
            Set<Author> writtenBy,
            Set<User> favoritedBy
    ) {
        setIsbn(isbn);
        setTitle(title);
        setPrice(price);
        setLocations(locations);
        setWrittenBy(writtenBy);
        setFavoritedBy(favoritedBy);
    }

    public Book(String isbn, String title, Double price, Set<Author> writtenBy, Set<User> favoritedBy) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.writtenBy = writtenBy;
        this.favoritedBy = favoritedBy;
    }

    public Book(String isbn, String title, Double price, Set<Author> writtenBy) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
        this.writtenBy = writtenBy;
    }

    public Book(String isbn, String title, Double price) {
        this.isbn = isbn;
        this.title = title;
        this.price = price;
    }

    // ----------------- Getters and Setters -----------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        // TODO: check if the isbn is valid!
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Set<Location> getLocations() {
        return locations;
    }

    public void setLocations(Set<Location> locations) {
        this.locations = locations;
    }

    public Set<Author> getWrittenBy() {
        return writtenBy;
    }

    public void setWrittenBy(Set<Author> writtenBy) {
        this.writtenBy = writtenBy;
    }

    public Set<User> getFavoritedBy() {
        return favoritedBy;
    }

    public void setFavoritedBy(Set<User> favoritedBy) {
        this.favoritedBy = favoritedBy;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id='" + id + '\'' +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", locations=" + locations +
                ", writtenBy=" + writtenBy +
                ", favoritedBy=" + favoritedBy +
                "}\n";
    }
}
