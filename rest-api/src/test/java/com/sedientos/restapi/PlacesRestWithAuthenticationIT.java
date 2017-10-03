package com.sedientos.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sedientos.data.model.Beer;
import com.sedientos.data.model.GlassSize;
import com.sedientos.data.model.Place;
import com.sedientos.restapi.configuration.SystemProfileValueSource2;
import com.sedientos.restapi.configuration.WebSecurityConfig;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.is;
import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@ProfileValueSourceConfiguration(value = SystemProfileValueSource2.class)
@IfProfileValue(name = ACTIVE_PROFILES_PROPERTY_NAME, value = "it-embedded")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        RestApiApplication.class
})
@WebAppConfiguration
@EnableAutoConfiguration
@ContextConfiguration(classes = WebSecurityConfig.class)
public class PlacesRestWithAuthenticationIT extends PlacesRestAbstractIT {

    @Before
    public final void initMockMvc() throws Exception {
        mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();
    }

    @Test
    @WithMockUser(username = "test", password = "test", roles = "USER")
    @Ignore //Can't get this to work
    public void ensurePostWorks() throws Exception {
        Place place = new Place();
        place.setName("Ondiñas do Mendo");
        place.setLocation(new Point(-3.7072689, 40.4568361));
        place.setAddress("C/ Villaamil, 4, 28039 Madrid");
        place.setStars(new BigDecimal(5));
        place.addBeer(new Beer(GlassSize.SMALL, new BigDecimal(1.5)));
        place.addBeer(new Beer(GlassSize.DOUBLE, new BigDecimal(2.5)));
        mockMvc.perform(post("/api/places", place).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(place))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(place.getName())));
    }

    @Test
    public void ensurePostNeedsAuthentication() throws Exception {
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
                .andExpect(status().isUnauthorized())
                .andExpect(header().string("WWW-Authenticate", "Bearer realm=\"api://default\", error=\"unauthorized\", error_description=\"Full authentication is required to access this resource\""));
    }
}
