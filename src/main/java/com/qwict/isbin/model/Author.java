package com.qwict.isbin.model;

import jakarta.persistence.*;

import java.util.Set;

// My authors are the owning side of books!
@Entity
@Table(name = "authors")
public class Author {
//  ----------------- Fields -----------------
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

//  Author is the owning side of books (an author has many books)
    @ManyToMany
    @JoinTable(name="author_books")
    public Set<Book> books;

//  ----------------- Constructors -----------------

    public Author() {}

    // constructor that allows author to be added without any books
    public Author(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
    }

    public Author(String firstName, String lastName, Set<Book> writtenBooks) {
        setFirstName(firstName);
        setLastName(lastName);
        setBooks(writtenBooks);
    }

//  ----------------- Getters and Setters -----------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    @Override
    public String toString() {
        return "Author{" +
                "id='" + id + '\'' +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
//                ", writtenBooks=" + books +
                '}';
    }
}
