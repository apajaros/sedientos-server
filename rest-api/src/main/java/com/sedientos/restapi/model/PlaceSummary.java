package com.sedientos.restapi.model;

import com.sedientos.data.model.Beer;
import com.sedientos.data.model.Place;
import org.springframework.data.geo.Point;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.util.List;

/**
 * View of a place that shows only the basic data to display a place on a map.
 */
@Projection(name="placeSummary", types={Place.class})
public interface PlaceSummary {

    String getName();
    Point getLocation();
    List<Beer> getBeers();
    BigDecimal getStars();

}
