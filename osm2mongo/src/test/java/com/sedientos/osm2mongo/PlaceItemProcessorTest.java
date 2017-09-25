package com.sedientos.osm2mongo;

import com.sedientos.data.model.Beer;
import com.sedientos.data.model.GlassSize;
import com.sedientos.data.model.Place;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PlaceItemProcessorTest {

    public static final int ID = 1;
    public static final BigDecimal LAT = new BigDecimal(4);
    public static final BigDecimal LON = new BigDecimal(18);
    public static final int OSM_VERSION = 987;
    public static final String PLACE_NAME = "place name";

    @Test
    public void noTagNameReturnsNullEntity() {
        NodeDTO node = getNodeDTO("website", "mywebsite");

        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            assertNull(processor.process(node));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void withTagNameReturnsNotNullEntity() {
        NodeDTO node = getNodeDTO("name", PLACE_NAME);

        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            assertNotNull(processor.process(node));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void attributesAreRead() {
        NodeDTO node = getNodeDTO("name", PLACE_NAME);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertNotNull(place.getLocation());
            assertEquals(LON.doubleValue(), place.getLocation().getX(), 0);
            assertEquals(LAT.doubleValue(), place.getLocation().getY(), 0);
            assertEquals(ID, place.getOsmId());
            assertEquals(OSM_VERSION, place.getOsmVersion());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void tagStreetIsRead() {
        String streetName = "My Street Name";
        NodeDTO node = getNodeDtoWithName("addr:street", streetName);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertEquals(streetName, place.getStreet());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void tagHousenumberIsRead() {
        String houseNumber = "My Street Number";
        NodeDTO node = getNodeDtoWithName("addr:housenumber", houseNumber);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertEquals(houseNumber, place.getHouseNumber());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void tagCityIsRead() {
        String city = "Madrid";
        NodeDTO node = getNodeDtoWithName("addr:city", city);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertEquals(city, place.getCity());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void tagPostalCodeIsRead() {
        String postalCode = "My Postal Code";
        NodeDTO node = getNodeDtoWithName("addr:postcode", postalCode);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertEquals(postalCode, place.getPostalCode());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void tagPhoneIsRead() {
        String phoneNumber = "phone numer";
        NodeDTO node = getNodeDtoWithName("phone", phoneNumber);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertEquals(phoneNumber, place.getPhoneNumber());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void tagContactPhoneIsRead() {
        String phoneNumber = "phone numer";
        NodeDTO node = getNodeDtoWithName("contact:phone", phoneNumber);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertEquals(phoneNumber, place.getPhoneNumber());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void tagWebsiteIsRead() {
        String website = "http://sedientos.com";
        NodeDTO node = getNodeDtoWithName("website", website);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertEquals(website, place.getWebsite());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void tagContactFacebookIsRead() {
        String website = "http://sedientos.com";
        NodeDTO node = getNodeDtoWithName("contact:facebook", website);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertEquals(website, place.getWebsite());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void tagEmailIsRead() {
        String email = "http://sedientos.com";
        NodeDTO node = getNodeDtoWithName("email", email);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertEquals(email, place.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void tagContactEmailIsRead() {
        String email = "http://sedientos.com";
        NodeDTO node = getNodeDtoWithName("contact:email", email);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertEquals(email, place.getEmail());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void tagOpeningHoursIsRead() {
        String openingHours = "opening hours";
        NodeDTO node = getNodeDtoWithName("opening_hours", openingHours);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            assertEquals(openingHours, place.getOpeningHours());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
    @Test
    public void placeHasBeers() {
        String openingHours = "opening hours";
        NodeDTO node = getNodeDtoWithName("opening_hours", openingHours);
        PlaceItemProcessor processor = new PlaceItemProcessor();
        try {
            Place place = processor.process(node);
            List<Beer> beers = place.getBeers();
            assertNotNull(beers);
            assertFalse(beers.isEmpty());
            assertEquals(GlassSize.SMALL, beers.get(0).getGlassSize());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    private NodeDTO getNodeDtoWithName(String key, String value) {
        NodeDTO node = getNodeDTO(key, value);
        node.getTags().add(new Tag("name", PLACE_NAME));
        return node;
    }

    private NodeDTO getNodeDTO(String key, String value) {
        NodeDTO node = getNodeDTO();
        List<Tag> tags = node.getTags();
        Tag tag = new Tag(key, value);
        tags.add(tag);
        node.setTags(tags);
        return node;
    }

    private NodeDTO getNodeDTO() {
        NodeDTO node = new NodeDTO();
        node.setId(ID);
        node.setLat(LAT);
        node.setLon(LON);
        node.setOsmVersion(OSM_VERSION);
        List<Tag> tags = new ArrayList<>();
        node.setTags(tags);
        return node;
    }

}