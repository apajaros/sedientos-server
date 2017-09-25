package com.sedientos.osm2mongo;

import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories()
public class MongoConfiguration {

    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception {
        MongoClientURI mongoClientURI = new MongoClientURI("mongodb://sed:kx6z0ecatmWIF8j1TqeJ@ds119524.mlab.com:19524/sedientosdb");
        return new SimpleMongoDbFactory(mongoClientURI);
    }

    public @Bean
    MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongoDbFactory());
    }
}