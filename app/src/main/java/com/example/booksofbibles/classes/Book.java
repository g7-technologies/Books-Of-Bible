package com.example.booksofbibles.classes;

public class Book {
    int id;



    int t_minutes;
    String name, bookType;

    public Book(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getT_minutes() {
        return t_minutes;
    }

    public void setT_minutes(int t_minutes) {
        this.t_minutes = t_minutes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }
}
