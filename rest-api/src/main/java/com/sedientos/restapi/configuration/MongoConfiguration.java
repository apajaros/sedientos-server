package com.sedientos.restapi.configuration;

import com.mongodb.Mongo;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.Arrays;
import java.util.Collection;

@Profile("default")
@Configuration
@EnableMongoRepositories(basePackages = { "com.sedientos.data.repository" })
public class MongoConfiguration extends AbstractMongoConfiguration {

    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception {
        MongoClientURI mongoClientURI = new MongoClientURI("mongodb://sed:kx6z0ecatmWIF8j1TqeJ@ds119524.mlab.com:19524/sedientosdb");
        return new SimpleMongoDbFactory(mongoClientURI);
    }

    public @Bean
    MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }

    @Override
    protected Collection<String> getMappingBasePackages() {
        return Arrays.asList("com.sedientos.data.model");
    }

    @Override
    protected String getDatabaseName() {
        return "sedientosdb";
    }

    @Override
    public Mongo mongo() throws Exception {
        return mongoDbFactory().getDb().getMongo();
    }
}