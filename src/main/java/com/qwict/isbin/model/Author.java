package com.qwict.isbin.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// My authors are the owning side of books!

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@ToString(exclude = "id")
@Entity
@Table(name = "authors", uniqueConstraints = { @UniqueConstraint(columnNames = { "first_name", "last_name" }) })
public class Author {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private String id;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="first_name", nullable=false)
    @NotBlank
    @Size(min=2, max=60)
    private String firstName;

    @Column(name="last_name", nullable=false)
    @NotBlank
    @Size(min=2, max=60)
    private String lastName;

//  Author is the owning side of books (an author has many books)
    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="authors_books",
            joinColumns={@JoinColumn(name="AUTHOR_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="BOOK_ID", referencedColumnName="ID")}
    )
    public List<Book> written = new ArrayList<>();
//    public Set<Book> books;

//  ----------------- Constructors -----------------

    public Author(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Author() {
    }

}
