package org.example.demodb;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private String imageLink;
    private int year;
    public Book(String title, String author, String isbn, String imageLink, int year) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.imageLink = imageLink;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getImageUrl() {
        return imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public int getYear() {
        return this.year;
    }

    @Override
    public String toString() {
        return "Book [title=" + title + ", author=" + author + ", isbn=" + isbn;
    }
}
