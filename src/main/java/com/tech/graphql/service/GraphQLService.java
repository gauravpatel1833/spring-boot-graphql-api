package com.tech.graphql.service;

import com.tech.graphql.constant.Category;
import com.tech.graphql.model.Author;
import com.tech.graphql.model.Book;
import com.tech.graphql.repository.AuthorRepository;
import com.tech.graphql.repository.BookRepository;
import com.tech.graphql.service.datafetcher.AllBooksDataFetcher;
import com.tech.graphql.service.datafetcher.AuthorDataFetcher;
import com.tech.graphql.service.datafetcher.BookDataFetcher;
import com.tech.graphql.service.datafetcher.CreateBookDataFetcher;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

@Service
public class GraphQLService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    /*Field which will load the graphsql file from resource which has schema definition*/
    @Value("classpath:books.graphql")
    Resource resource;

    /*Object from graphQL library which will be used to query the data*/
    private GraphQL graphQL;

    /*Different types of fetchers based on query defined in books.graphql*/
    @Autowired
    private AllBooksDataFetcher allBooksDataFetcher;

    @Autowired
    private BookDataFetcher bookDataFetcher;

    @Autowired
    CreateBookDataFetcher createBookDataFetcher;

    @Autowired
    AuthorDataFetcher authorDataFetcher;

    @PostConstruct
    private void loadSchema() throws IOException {

        //Load Books into the Book Repository
        loadDataIntoHSQL();

        //get the schema
        File schemaFile = resource.getFile();

        //parse schema : will read data from the schema
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildRuntimeWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(typeRegistry,wiring);
        graphQL = GraphQL.newGraphQL(schema).build();

    }

   /* RuntimeWiring for schema "Query" and its two queries allBooks and book
   * So based on the request it will call the respective data fetcher which will query the database*/
    private RuntimeWiring buildRuntimeWiring() {

        System.out.println("Inside run time wiring");

        //Below is alternative to add wiring
         RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query").dataFetcher("allBooks",allBooksDataFetcher))
                .type(TypeRuntimeWiring.newTypeWiring("Query").dataFetcher("book",bookDataFetcher))
                .type(TypeRuntimeWiring.newTypeWiring("Mutation").dataFetcher("createBook",createBookDataFetcher))
                 .type(TypeRuntimeWiring.newTypeWiring("Book").dataFetcher("author",authorDataFetcher))
                .build();

        /*return RuntimeWiring.newRuntimeWiring()
                .type("Query",typeWiring -> typeWiring
                        .dataFetcher("allBooks",allBooksDataFetcher)
                        .dataFetcher("book",bookDataFetcher))
                .type("Mutation",typeWiring -> typeWiring
                        .dataFetcher("createBook",createBookDataFetcher))
                .build();*/
        return runtimeWiring;
    }

    /*To load the dummy data in database*/
    private void loadDataIntoHSQL() {

        Stream.of(
                new Book("123", "Book of Clouds", "Kindle Edition",
                        new String[] {
                                "Chloe Aridjis"
                        }, "Nov 2017", Category.COMEDY),
                new Book("124", "Cloud Arch & Engineering", "Orielly",
                        new String[] {
                                "Peter", "Sam"
                        }, "Jan 2015",Category.FANTASY),
                new Book("125", "Java 9 Programming", "Orielly",
                        new String[] {
                                "Venkat", "Ram"
                        }, "Dec 2016",Category.HORROR)
        ).forEach(book -> {
            bookRepository.save(book);
        });

        Stream.of(
                new Author("1","Gaurav",31,"123"),
                new Author("2","Nayan",32,"124"),
                new Author("3","Pratik",30,"125")
        ).forEach(author -> {
            authorRepository.save(author);
        });
    }

    //To return the object to be used in controller
    public GraphQL getGraphQL(){
        return graphQL;
    }
}
