package com.sedientos.restapi.repository;

import com.sedientos.data.model.Place;
import com.sedientos.restapi.model.PlaceSummary;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigDecimal;
import java.util.Collection;

@CrossOrigin
@RepositoryRestResource(excerptProjection = PlaceSummary.class)
public interface PlaceRepository extends MongoRepository<Place, String> {

    Collection<Place> findByLocationNear(@Param("point")Point point, @Param("max_distance") Distance maxDistance);
    Collection<Place> findByLocationWithin(@Param("box")Box box);
    Collection<Place> findByStarsGreaterThanEqual(@Param("stars") BigDecimal stars);
    @Query("{ 'beers.price' : { $lte: ?0 } }")
    Collection<Place> findByBeersPriceLesserThanEqual(@Param("price")BigDecimal price);
    //TODO Search with normalization
    Collection<Place> findByAddressContaining(@Param("address") String address);

}