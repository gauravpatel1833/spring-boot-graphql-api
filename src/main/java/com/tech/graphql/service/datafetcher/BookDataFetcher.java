package com.tech.graphql.service.datafetcher;

import com.tech.graphql.model.Book;
import com.tech.graphql.repository.BookRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookDataFetcher implements DataFetcher<Book> {

    /*Class implement data fetcher based on the parameter it return and override get method*/

    @Autowired
    BookRepository bookRepository;

    @Override
    public Book get(DataFetchingEnvironment dataFetchingEnvironment) {

        /*book(id: String): Book in graphql based on the query from the request it will fetch the value*/
        String isn = dataFetchingEnvironment.getArgument("id");

        return bookRepository.findById(isn).get();
    }
}
