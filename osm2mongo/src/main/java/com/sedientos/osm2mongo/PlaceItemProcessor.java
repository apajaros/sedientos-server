package com.sedientos.osm2mongo;

import com.sedientos.data.model.Beer;
import com.sedientos.data.model.GlassSize;
import com.sedientos.data.model.Place;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.data.geo.Point;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlaceItemProcessor implements ItemProcessor<NodeDTO, Place> {

    private static final Logger log = LoggerFactory.getLogger(PlaceItemProcessor.class);

    @Override
    public Place process(NodeDTO node) throws Exception {
//        log.info("Processing node " + node);
        Place place = new Place();
        Point location = new Point(node.getLon().doubleValue(), node.getLat().doubleValue());
        place.setLocation(location);
        place.setOsmId(node.getId());
        place.setOsmVersion(node.getOsmVersion());
        for (Tag tag : node.getTags()) {
            String key = tag.getK();
            switch (key) {
                case "name":
                    place.setName(tag.getV());
                    break;
                case "addr:street":
                    place.setStreet(tag.getV());
                    break;
                case "addr:housenumber":
                    place.setHouseNumber(tag.getV());
                    break;
                case "addr:city":
                    place.setCity(tag.getV());
                    break;
                case "addr:postcode":
                    place.setPostalCode(tag.getV());
                    break;
                case "phone":
                case "contact:phone":
                    place.setPhoneNumber(tag.getV());
                    break;
                case "website":
                case "contact:facebook":
                    place.setWebsite(tag.getV());
                    break;
                case "email":
                case "contact:email":
                    place.setEmail(tag.getV());
                    break;
                case "opening_hours":
                    place.setOpeningHours(tag.getV());
                    break;
            }
        }
        if (place.getName() == null) {
            // If we don't even have the place name, we don't want to add it to the collection
            return null;
        }

        place.setAddress(getFullAddress(place));
        place.setBeers(generateRandomBeers());
        return place;
    }

    private String getFullAddress(Place place) {
        String street = place.getStreet();
        String address = null;
        if (street != null && !street.isEmpty()) {
            address = String.format("%s, %s %s %s",
                    getEmptyIfNull(street),
                    getEmptyIfNull(place.getHouseNumber()),
                    getEmptyIfNull(place.getPostalCode()),
                    getEmptyIfNull(place.getCity()));
            address = address.trim();
        }
        return address;
    }

    private String getEmptyIfNull(String s) {
        return s == null ? "" : s;
    }

    private List<Beer> generateRandomBeers() {
        List<Beer> beers = new ArrayList<>();
        Random r = new Random();

        beers.add(new Beer(GlassSize.SMALL, 1 + r.nextDouble()));
        if (r.nextDouble() > 0.3) {
            beers.add(new Beer(GlassSize.DOUBLE, 2 + r.nextDouble()));
        }
        if (r.nextDouble() > 0.8) {
            beers.add(new Beer(GlassSize.PINT, 4 + r.nextDouble()));
        }
        return beers;
    }

}
