package com.tech.graphql.service.datafetcher;

import com.tech.graphql.model.Author;
import com.tech.graphql.model.Book;
import com.tech.graphql.repository.AuthorRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthorDataFetcher implements DataFetcher<Author> {

    @Autowired
    AuthorRepository authorRepository;

    @Override
    public Author get(DataFetchingEnvironment dataFetchingEnvironment) {
        Book book = dataFetchingEnvironment.getSource();
        return authorRepository.getByBookId(book.getIsn());
    }
}
