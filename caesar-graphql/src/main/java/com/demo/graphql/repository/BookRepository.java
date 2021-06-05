package com.demo.graphql.repository;

import com.demo.graphql.entity.Books;

/**
 *
 */
public interface BookRepository {
    Books getByIsbn(String isbn);
}
