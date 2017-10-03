package com.sedientos.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sedientos.data.model.Beer;
import com.sedientos.data.model.GlassSize;
import com.sedientos.data.model.Place;
import com.sedientos.restapi.configuration.SystemProfileValueSource2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ProfileValueSourceConfiguration(value = SystemProfileValueSource2.class)
@IfProfileValue(name = ACTIVE_PROFILES_PROPERTY_NAME, value = "it-embedded")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        RestApiApplication.class
})
@WebAppConfiguration
@EnableAutoConfiguration
public class PlacesRestIT extends PlacesRestAbstractIT {

    @Test
    public void getPlaces() throws Exception {
        String urlTemplate = "/api/places/";
        mockMvc.perform(get(urlTemplate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("$.page.totalElements", is(2)));
    }

    @Test
    public void getSinglePlace() throws Exception {
        String urlTemplate = "/api/places/" + this.placeList.get(0).getId();
        mockMvc.perform(get(urlTemplate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("$.name", is("Ondiñas do Mendo")))
                .andExpect(jsonPath("$.stars", is(5)))
                .andExpect(jsonPath("$._links.self.href", is("http://localhost" + urlTemplate)));
    }

    @Test
    public void ensurePostWorks() throws Exception {
        Place place = new Place();
        place.setName("Ondiñas do Mendo");
        place.setLocation(new Point(-3.7072689, 40.4568361));
        place.setAddress("C/ Villaamil, 4, 28039 Madrid");
        place.setStars(new BigDecimal(5));
        place.addBeer(new Beer(GlassSize.SMALL, new BigDecimal(1.5)));
        place.addBeer(new Beer(GlassSize.DOUBLE, new BigDecimal(2.5)));
        mockMvc.perform(post("/api/places", place).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(place)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(place.getName())));
    }

}
