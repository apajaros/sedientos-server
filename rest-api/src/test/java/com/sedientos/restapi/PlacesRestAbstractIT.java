package com.sedientos.restapi;

import com.sedientos.data.model.*;
import com.sedientos.restapi.repository.PlaceRepository;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public class PlacesRestAbstractIT {
    protected MockMvc mockMvc;
    protected List<Place> placeList = new ArrayList<>();
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    protected WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        this.placeRepository.deleteAll();

        User aUser = new User("apajaros", "email@mail.com");
        User anotherUser = new User("Frodo", "frodo@hobbiton.com");

        Place aPlace = new Place();
        aPlace.setName("Ondiñas do Mendo");
        aPlace.setLocation(new Point(-3.7072689, 40.4568361));
        aPlace.setAddress("C/ Villaamil, 4, 28039 Madrid");
        aPlace.setStars(new BigDecimal(5));
        aPlace.addBeer(new Beer(GlassSize.SMALL, new BigDecimal(1.5)));
        aPlace.addBeer(new Beer(GlassSize.DOUBLE, new BigDecimal(2.5)));
        aPlace.addReview(new Review(aUser, 5, "Great!", new Date()));
        aPlace.addReview(new Review(anotherUser, 5, "Awesome!", new Date()));
        Place anotherPlace = new Place();
        anotherPlace.setName("Bar Manolo");
        anotherPlace.setLocation(new Point(-3.5130504, 40.4634321));
        anotherPlace.setAddress("C/ Inventada, 134, 28028 Madrid");
        anotherPlace.setStars(new BigDecimal(3.5));
        anotherPlace.addBeer(new Beer(GlassSize.SMALL, new BigDecimal(1.8)));
        anotherPlace.addBeer(new Beer(GlassSize.DOUBLE, new BigDecimal(3)));
        anotherPlace.addReview(new Review(aUser, 4, "Nice place!", new Date()));
        anotherPlace.addReview(new Review(anotherUser, 3, "Meh", new Date()));
        // save a couple of Places
        Place savedPlace = placeRepository.save(aPlace);
        Place anotherSavedPlace = placeRepository.save(anotherPlace);

        placeList.add(savedPlace);
        placeList.add(anotherSavedPlace);
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
