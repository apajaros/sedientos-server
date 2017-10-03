package com.sedientos.restapi;

import com.mongodb.DBCollection;
import com.sedientos.data.model.Beer;
import com.sedientos.data.model.GlassSize;
import com.sedientos.data.model.Place;
import com.sedientos.restapi.configuration.SystemProfileValueSource2;
import com.sedientos.restapi.repository.PlaceRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

@ProfileValueSourceConfiguration(value = SystemProfileValueSource2.class)
@IfProfileValue(name = ACTIVE_PROFILES_PROPERTY_NAME, value = "it-embedded")
@RunWith(SpringRunner.class)
@DataMongoTest
public class MongoSliceIT {

    String collectionName;
    Place place;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private PlaceRepository placeRepository;

    @Before
    public void before() {
        collectionName = "places";
        place = new Place();
        place.setName("Ondiñas do Mendo");
        place.setLocation(new Point(-3.7072689, 40.4568361));
        place.setAddress("C/ Villaamil, 4, 28039 Madrid");
        place.setStars(new BigDecimal(5));
        place.addBeer(new Beer(GlassSize.SMALL, new BigDecimal(1.5)));
        place.addBeer(new Beer(GlassSize.DOUBLE, new BigDecimal(2.5)));
    }

    @After
    public void after() {
        mongoTemplate.dropCollection(collectionName);
    }

    @Test
    public void checkMongoTemplate() {
        assertNotNull(mongoTemplate);
        DBCollection createdCollection = mongoTemplate.createCollection(collectionName);
        assertTrue(mongoTemplate.collectionExists(collectionName));
    }

    @Test
    public void checkDocumentAndQuery() {
        mongoTemplate.save(place, collectionName);
        Query query = new Query(new Criteria()
                .andOperator(Criteria.where("stars").regex(place.getStars().toString()),
                        Criteria.where("address").regex(place.getAddress())));

        Place retrievedPlace = mongoTemplate.findOne(query, Place.class, collectionName);
        assertNotNull(retrievedPlace);
    }

    @Test
    public void checkPlaceRepository() {
        assertNotNull(placeRepository);
        Place savedPlace = placeRepository.save(place);
        assertNotNull(placeRepository.findOne(savedPlace.getId()));
    }

}
