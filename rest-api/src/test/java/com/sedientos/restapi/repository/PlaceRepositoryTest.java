package com.sedientos.restapi.repository;

import com.sedientos.data.model.Beer;
import com.sedientos.data.model.GlassSize;
import com.sedientos.data.model.Place;
import com.sedientos.restapi.RestApiApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {
        RestApiApplication.class
})
public class PlaceRepositoryTest {

    @Autowired
    PlaceRepository placeRepository;

    @Before
    public void setUp() {
        placeRepository.deleteAll();
    }

    @Test
    public void findByLocationNear() throws Exception {
        Place aPlace = new Place();
        aPlace.setName("Ondiñas do Mendo");
        aPlace.setLocation(new Point(0, 0));
        aPlace.setAddress("C/ Villaamil, 4, 28039 Madrid");
        aPlace.setStars(new BigDecimal(5));
        aPlace.addBeer(new Beer(GlassSize.SMALL, new BigDecimal(1.5)));
        aPlace.addBeer(new Beer(GlassSize.DOUBLE, new BigDecimal(2.5)));
        placeRepository.save(aPlace);

        Point point = new Point(0, 0);
        Distance distance = new Distance(0);
        Collection<Place> retrievedPlaces = placeRepository.findByLocationNear(point, distance);
        assertEquals(1, retrievedPlaces.size());
    }

    @Test
    public void findByLocationWithin() throws Exception {
        Place aPlace = new Place();
        aPlace.setName("Ondiñas do Mendo");
        aPlace.setLocation(new Point(0, 0));
        aPlace.setAddress("C/ Villaamil, 4, 28039 Madrid");
        aPlace.setStars(new BigDecimal(5));
        aPlace.addBeer(new Beer(GlassSize.SMALL, new BigDecimal(1.5)));
        aPlace.addBeer(new Beer(GlassSize.DOUBLE, new BigDecimal(2.5)));
        placeRepository.save(aPlace);

        Point firstPoint = new Point(0, 0);
        Point secondPoint = new Point(1, 1);
        Box box = new Box(firstPoint, secondPoint);
        Collection<Place> retrievedPlaces = placeRepository.findByLocationWithin(box);
        assertEquals(1, retrievedPlaces.size());
    }

    @Test
    public void findByStarsGreaterThanEqual() throws Exception {
        Place aPlace = new Place();
        aPlace.setName("Ondiñas do Mendo");
        aPlace.setLocation(new Point(0, 0));
        aPlace.setAddress("C/ Villaamil, 4, 28039 Madrid");
        aPlace.setStars(new BigDecimal(5));
        placeRepository.save(aPlace);

        Place anotherPlace = new Place();
        anotherPlace.setName("Ondiñas do Mendo");
        anotherPlace.setLocation(new Point(0, 0));
        anotherPlace.setAddress("C/ Villaamil, 4, 28039 Madrid");
        anotherPlace.setStars(new BigDecimal(4));
        placeRepository.save(anotherPlace);

        Collection<Place> retrievedPlaces = placeRepository.findByStarsGreaterThanEqual(new BigDecimal(5));
        assertEquals(1, retrievedPlaces.size());
    }

    @Test
    public void findByBeersPriceLesserThanEqual() throws Exception {
        Place aPlace = new Place();
        aPlace.setName("Ondiñas do Mendo");
        aPlace.setLocation(new Point(0, 0));
        aPlace.setAddress("C/ Villaamil, 4, 28039 Madrid");
        aPlace.setStars(new BigDecimal(5));
        aPlace.addBeer(new Beer(GlassSize.SMALL, new BigDecimal(1.5)));
        aPlace.addBeer(new Beer(GlassSize.DOUBLE, new BigDecimal(2.5)));
        placeRepository.save(aPlace);

        Place anotherPlace = new Place();
        anotherPlace.setName("Ondiñas do Mendo");
        anotherPlace.setLocation(new Point(0, 0));
        anotherPlace.setAddress("C/ Villaamil, 4, 28039 Madrid");
        anotherPlace.setStars(new BigDecimal(4));
        anotherPlace.addBeer(new Beer(GlassSize.SMALL, new BigDecimal(1.6)));
        anotherPlace.addBeer(new Beer(GlassSize.DOUBLE, new BigDecimal(2.5)));
        placeRepository.save(anotherPlace);

        Collection<Place> retrievedPlaces = placeRepository.findByBeersPriceLesserThanEqual(new BigDecimal(1.5));
        assertEquals(1, retrievedPlaces.size());
    }

    @Test
    public void findByAddressContaining() throws Exception {
        Place aPlace = new Place();
        aPlace.setName("Ondiñas do Mendo");
        aPlace.setLocation(new Point(0, 0));
        aPlace.setAddress("C/ Villaamil, 4, 28039 Madrid");
        placeRepository.save(aPlace);

        Place anotherPlace = new Place();
        anotherPlace.setName("Ondiñas do Mendo");
        anotherPlace.setLocation(new Point(0, 0));
        anotherPlace.setAddress("C/ Otra, 4, 28039 Madrid");
        placeRepository.save(anotherPlace);

        Collection<Place> retrievedPlaces = placeRepository.findByAddressContaining("Villaamil");
        assertEquals(1, retrievedPlaces.size());
    }

}