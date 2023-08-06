package com.weather.api.weatherStatus.config;

import com.weather.api.weatherStatus.dto.RequestCity;
import com.weather.api.weatherStatus.dto.WeatherDto;
import com.weather.api.weatherStatus.resolver.WeatherMutationResolver;
import com.weather.api.weatherStatus.resolver.WeatherResolver;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Configuration
public class GraphQLConfig {

     private final WeatherResolver weatherResolver;
    private final WeatherMutationResolver mutationResolver;

    private final ResourceLoader resourceLoader;

    public GraphQLConfig(WeatherResolver weatherResolver, WeatherMutationResolver mutationResolver, ResourceLoader resourceLoader) {
        this.weatherResolver = weatherResolver;
        this.mutationResolver = mutationResolver;
        this.resourceLoader = resourceLoader;
    }

    @Bean
    public GraphQL graphQL() throws IOException {
        Resource schemaResource = resourceLoader.getResource("classpath:graphql/schema.graphqls");
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(schemaResource.getInputStream());
        RuntimeWiring runtimeWiring = buildRuntimeWiring(weatherResolver,mutationResolver);

        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring);
        return GraphQL.newGraphQL(graphQLSchema).build();
    }

    @Bean
    public RuntimeWiring buildRuntimeWiring(WeatherResolver weatherResolver, WeatherMutationResolver mutationResolver) {
        DataFetcher<WeatherDto> getDefaultWeatherFetcher = environment -> weatherResolver.getDefaultWeather();

        DataFetcher<WeatherDto> getWeatherFetcher = environment ->(mutationResolver.getWeather((RequestCity) environment.getArgument("requestCity")));
        DataFetcher<List<WeatherDto>> getForecastFetcher = environment -> mutationResolver.getForecast((RequestCity) environment.getArgument("requestCity"));
        DataFetcher<List<WeatherDto>> getForecastDailyFetcher = environment -> mutationResolver.getForecastDaily((RequestCity) environment.getArgument("requestCity"));

        TypeRuntimeWiring.Builder queryTypeBuilder = TypeRuntimeWiring.newTypeWiring("Query")
                .dataFetcher("getDefaultWeather", getDefaultWeatherFetcher);

        TypeRuntimeWiring.Builder mutationTypeBuilder = TypeRuntimeWiring.newTypeWiring("Mutation")
                .dataFetcher("getWeather", getWeatherFetcher)
                .dataFetcher("getForecast", getForecastFetcher)
                .dataFetcher("getForecastDaily", getForecastDailyFetcher);

        return RuntimeWiring.newRuntimeWiring()
                .type(queryTypeBuilder)
                .type(mutationTypeBuilder)
                .build();
    }
}

