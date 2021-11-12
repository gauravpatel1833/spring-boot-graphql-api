package com.tech.graphql.resource;

import com.tech.graphql.service.GraphQLService;
import graphql.ExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rest/books")
@RestController
public class BookResource {

    /*GraphQL is a way to fetch data based on the request. It is a framework/Query Language.
    It doesn't replace Rest as Rest is an architecture Style.
    So client will send the request with field data it required in the result.
    So the URL will remain same but different client can send different request and get different results.
    GraphQL does server side filtering*/

    /*GraphQLService which will define the data fetchers and do the parsing of the data based on request*/
    @Autowired
    GraphQLService graphQLService;

    /*GraphQL uses only POST method unlike Rest which uses all http methods*/
    @PostMapping
    public ResponseEntity<ExecutionResult> getAllBooks(@RequestBody String query){

        /*Generic Method for Books and based on query it will return the result*/
        ExecutionResult execute = graphQLService.getGraphQL().execute(query);

        return new ResponseEntity<>(execute, HttpStatus.OK);

    }
}
