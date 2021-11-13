package com.tech.graphql.repository;

import com.tech.graphql.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author,String> {
    Author getByBookId(String isn);
}
