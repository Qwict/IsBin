package com.qwict.isbin.model;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "locations")
public class Location {
//  ----------------- Fields -----------------
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name="name")
    private String name;

    @Column(name="place_code_1")
    private Integer placeCode1;

    @Column(name="place_code_2")
    private Integer placeCode2;

    // Location is the inverse side; this means that it is owned by a Book
    @ManyToOne
    @JoinColumn(name="BOOK_ID", nullable=false)
    private Book book;

//  ----------------- Constructors -----------------
    public Location() {}
    public Location(
            String name,
            Integer placeCode1,
            Integer placeCode2,
            Book book
    ) {
        setName(name);
        setPlaceCode1(placeCode1);
        setPlaceCode2(placeCode2);
        setBook(book);
    }


//  ----------------- Getters and Setters -----------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPlaceCode1() {
        return placeCode1;
    }

    public void setPlaceCode1(Integer placeCode1) {
        this.placeCode1 = placeCode1;
    }

    public Integer getPlaceCode2() {
        return placeCode2;
    }

    public void setPlaceCode2(Integer placeCode2) {
        this.placeCode2 = placeCode2;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", placeCode1=" + placeCode1 +
                ", placeCode2=" + placeCode2 +
//                ", book=" + book +
                '}';
    }
}
