package com.qwict.isbin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

// My authors are the owning side of books!

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

    // constructor that allows author to be added without any books
    public Author(String firstName, String lastName) {
        setFirstName(firstName);
        setLastName(lastName);
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
