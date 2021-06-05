package com.demo.graphql.helloworld;

import com.demo.graphql.helloworld.GraphQLDataFetchers;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {
    // an init method which will create a GraphQL instance

    // The GraphQL Java Spring adapter will use that GraphQL instance to make our schema available
    // via HTTP on the default url /graphql.

    @Autowired
    GraphQLDataFetchers graphQLDataFetchers;
    private GraphQL graphQL;

    @Bean
    public GraphQL graphQL() {
        // This GraphQL instance is exposed as a Spring Bean via the graphQL() method annotated with @Bean.
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        // We use Guava Resources to read the file from our classpath
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        // then create a GraphQLSchema and GraphQL instance.
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        // TypeDefinitionRegistry is the parsed version of our schema file.
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        // SchemaGenerator combines the TypeDefinitionRegistry with RuntimeWiring to actually make the GraphQLSchema.
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("bookById", graphQLDataFetchers.getBookByIdDataFetcher()))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build();
    }

}
