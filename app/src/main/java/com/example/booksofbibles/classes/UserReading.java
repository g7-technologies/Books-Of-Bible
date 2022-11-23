package com.example.booksofbibles.classes;

public class UserReading {

    String book_name,image_path;
    int total_min;

    public UserReading(String book_name, String image_path, int total_min) {
        this.book_name = book_name;
        this.image_path = image_path;
        this.total_min = total_min;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public int getTotal_min() {
        return total_min;
    }

    public void setTotal_min(int total_min) {
        this.total_min = total_min;
    }
}
