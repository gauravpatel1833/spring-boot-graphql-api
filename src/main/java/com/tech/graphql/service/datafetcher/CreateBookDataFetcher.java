package com.tech.graphql.service.datafetcher;

import com.tech.graphql.model.Book;
import com.tech.graphql.repository.BookRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateBookDataFetcher implements DataFetcher<Book> {

    @Autowired
    BookRepository bookRepository;

    @Override
    public Book get(DataFetchingEnvironment dataFetchingEnvironment) {

        String isn = dataFetchingEnvironment.getArgument("isn");
        String title = dataFetchingEnvironment.getArgument("title");
        Book book = new Book();
        book.setIsn(isn);
        book.setTitle(title);
        Book savedBook = bookRepository.save(book);
        return savedBook;
    }
}
