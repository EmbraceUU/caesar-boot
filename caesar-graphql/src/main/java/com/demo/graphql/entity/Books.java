package com.demo.graphql.entity;

import lombok.Data;

@Data
public class Books {

    private String isbn;
    private String title;

    public Books(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book{" + "isbn='" + isbn + '\'' + ", title='" + title + '\'' + '}';
    }
}
