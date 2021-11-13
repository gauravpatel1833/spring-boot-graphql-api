package com.tech.graphql.service.datafetcher;

import com.tech.graphql.constant.Category;
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

        System.out.println("Inside Create book");

        String isn = dataFetchingEnvironment.getArgument("isn");
        String title = dataFetchingEnvironment.getArgument("title");
        String publisher = dataFetchingEnvironment.getArgument("publisher");

        //For type Enums
        String categoryVal = dataFetchingEnvironment.getArgument("category");
        Category category = Category.valueOf(categoryVal);

        Book book = new Book();
        book.setIsn(isn);
        book.setTitle(title);
        book.setPublisher(publisher);
        book.setCategory(category);
        Book savedBook = bookRepository.save(book);
        return savedBook;
    }
}
