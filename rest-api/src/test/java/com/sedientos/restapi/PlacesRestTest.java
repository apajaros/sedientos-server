package com.sedientos.restapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sedientos.data.model.*;
import com.sedientos.restapi.configuration.SystemProfileValueSource2;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.hypermedia.HypermediaDocumentation;
import org.springframework.restdocs.hypermedia.LinkDescriptor;
import org.springframework.restdocs.hypermedia.LinksSnippet;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.annotation.ProfileValueSourceConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
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
public class PlacesRestTest extends PlacesRestAbstractIT {

    @Rule
    public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Override
    @Before
    public void setup() throws Exception {
        super.setup();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .apply(documentationConfiguration(this.restDocumentation))
                .build();
    }

    @Test
    public void getIndex() throws Exception {
        this.mockMvc.perform(get("/api/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("index"));
    }

    @Test
    public void getPlaces() throws Exception {
        String urlTemplate = "/api/places/";
        mockMvc.perform(get(urlTemplate))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json;charset=UTF-8"))
                .andExpect(jsonPath("$.page.totalElements", is(2)))
                .andDo(document("places-list-example",
                    responseFields(
                        subsectionWithPath("_embedded.places").description("An array of <<resources-places, Place resources>>"),
                        subsectionWithPath("_links").description("<<resources-places-list-links, Links>> to other resources"),
                        fieldWithPath("page").description("Description of the current page of results"),
                        fieldWithPath("page.size").description("The size of the page of results"),
                        fieldWithPath("page.totalElements").description("The total number of results"),
                        fieldWithPath("page.totalPages").description("The amount of pages of results"),
                        fieldWithPath("page.number").description("The number of the current page of results"))));
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
                .andExpect(jsonPath("$._links.self.href", is("http://localhost:8080" + urlTemplate)))
                .andDo(document("places-get-example",
                    links(
                        linkWithRel("self").description("Canonical link for this <<resources-places,place>>"),
                        linkWithRel("place").description("This <<resources-places,place>>")),
                    responseFields(
                        fieldWithPath("name").description("The name of the place"),
                        fieldWithPath("location").description("The location of the place"),
                        fieldWithPath("location.lng").description("The longitude of the location"),
                        fieldWithPath("location.lat").description("The latitude of the location"),
                        fieldWithPath("address").description("The address of the place"),
                        fieldWithPath("stars").description("The star rating of the place"),
                        fieldWithPath("phoneNumber").description("The phone number of the place").type(JsonFieldType.STRING).optional(),
                        fieldWithPath("website").description("The website of the place").type(JsonFieldType.STRING).optional(),
                        fieldWithPath("email").description("The email of the place").type(JsonFieldType.STRING).optional(),
                        fieldWithPath("openingHours").description("The opening hours rating of the place").type(JsonFieldType.STRING).optional(),
                        fieldWithPath("street").description("The street of the place").type(JsonFieldType.STRING).optional(),
                        fieldWithPath("houseNumber").description("The house number of the place").type(JsonFieldType.STRING).optional(),
                        fieldWithPath("city").description("The city of the place").type(JsonFieldType.STRING).optional(),
                        fieldWithPath("postalCode").description("The postal code of the place").type(JsonFieldType.STRING).optional(),
                        subsectionWithPath("beers").description("The beers of the place"),
                        subsectionWithPath("reviews").description("A list of reviews of the place"),
                        subsectionWithPath("_links").description("<<resources-places-links,Links>> to other resources"))));
    }

    @Test
    public void postPlace() throws Exception {
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
                .andExpect(jsonPath("$.name", is(place.getName())))
                .andDo(document("places-create-example",
                    requestFields(
                        fieldWithPath("name").description("The name of the place"),
                        fieldWithPath("location").description("The location of the place"),
                        fieldWithPath("location.lng").description("The longitude of the location"),
                        fieldWithPath("location.lat").description("The latitude of the location"),
                        fieldWithPath("address").description("The address of the place"),
                        fieldWithPath("stars").description("The star rating of the place"),
                        fieldWithPath("beers").description("The beers of the place"),
                        fieldWithPath("beers[].glassSize").description("The size of the beer"),
                        fieldWithPath("beers[].price").description("The price of the beer"),
                        fieldWithPath("reviews").description("A list of reviews of the place"))));
    }

    @Test
    public void searchFindByStarsGreaterThanEqual() throws Exception {
        String urlTemplate = "/api/places/search/findByStarsGreaterThanEqual?stars=4";
        mockMvc.perform(get(urlTemplate))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("places-search-stars-example",
                        requestParameters(
                                parameterWithName("stars").description("The minimum amount of stars of the places to search")),
                        responseFields(
                                subsectionWithPath("_embedded").description("A list of places with the given criteria"),
                                subsectionWithPath("_links").ignored())));
    }

    @Test
    public void searchFindByLocationWithin() throws Exception {
        String urlTemplate = "/api/places/search/findByLocationWithin?box=-3.7072689,40.4568361,-3.5130504, 40.4634321";
        mockMvc.perform(get(urlTemplate))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("places-search-within-example",
                        requestParameters(
                                parameterWithName("box").description("The two pair of coordinates that define the area to search")),
                        responseFields(
                                subsectionWithPath("_embedded").description("A list of places with the given criteria"),
                                subsectionWithPath("_links").ignored())));
    }

    @Test
    public void searchFindByBeersPriceLesserThanEqual() throws Exception {
        String urlTemplate = "/api/places/search/findByBeersPriceLesserThanEqual?price=1.5";
        mockMvc.perform(get(urlTemplate))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("places-search-price-example",
                        requestParameters(
                                parameterWithName("price").description("The maximum price of a beer in the places to search")),
                        responseFields(
                                subsectionWithPath("_embedded").description("A list of places with the given criteria"),
                                subsectionWithPath("_links").ignored())));
    }

    @Test
    public void searchFindByAddressContaining() throws Exception {
        String urlTemplate = "/api/places/search/findByAddressContaining?address=Villaamil";
        mockMvc.perform(get(urlTemplate))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("places-search-address-example",
                        requestParameters(
                                parameterWithName("address").description("The address we want to search. It is case sensitive.")),
                        responseFields(
                                subsectionWithPath("_embedded").description("A list of places with the given criteria"),
                                subsectionWithPath("_links").ignored())));
    }

    @Test
    public void searchFindByLocationNear() throws Exception {
        String urlTemplate = "/api/places/search/findByLocationNear?point=-3.7072689,40.4568361&max_distance=1km";
        mockMvc.perform(get(urlTemplate))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("places-search-location-example",
                        requestParameters(
                                parameterWithName("point").description("The center of the area to search"),
                                parameterWithName("max_distance").description("The radius of the area to search. You can specify the units by adding 'km' or 'mi'. If empty, it is considered to be radians.").optional()),
                        responseFields(
                                subsectionWithPath("_embedded").description("A list of places with the given criteria"),
                                subsectionWithPath("_links").ignored())));
    }

    @Test
    public void getReviews() throws Exception {
        String urlTemplate = "/api/places/" + placeList.get(0).getId() + "/reviews";
        mockMvc.perform(get(urlTemplate))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("places-reviews-list-example",
                        relaxedResponseFields(
                                fieldWithPath("_embedded.reviews[]").description("A list of reviews of the place"),
                                subsectionWithPath("_embedded.reviews[].user").description("The user that rated the place"),
                                fieldWithPath("_embedded.reviews[].stars").description("The star rating given to the place on this review"),
                                fieldWithPath("_embedded.reviews[].body").description("The body of the review").optional().type(JsonFieldType.STRING),
                                fieldWithPath("_embedded.reviews[].date").description("Date (with time) of the review"),
                                subsectionWithPath("_links").ignored())));
    }

    @Test
    public void postReviews() throws Exception {
        Review r = new Review();
        r.setStars(2);
        r.setBody("I didn't like it that much.");
        r.setUser(new User("name", "email@mail.com"));
        String urlTemplate = "/api/places/" + placeList.get(0).getId() + "/reviews";
        mockMvc.perform(post(urlTemplate, r).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(r)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("places-reviews-create-example",
                        relaxedResponseFields(
                                fieldWithPath("_embedded.reviews[]").description("A list of reviews of the place"),
                                subsectionWithPath("_embedded.reviews[].user").description("The user that rated the place"),
                                fieldWithPath("_embedded.reviews[].stars").description("The star rating given to the place on this review"),
                                fieldWithPath("_embedded.reviews[].body").description("The body of the review").optional().type(JsonFieldType.STRING),
                                fieldWithPath("_embedded.reviews[].date").description("Date (with time) of the review"),
                                subsectionWithPath("_links").ignored())));
    }

    @Test
    @Ignore
    public void errorExample() throws Exception {
        String urlTemplate = "/api/places/1";
        Place place = new Place();
        place.setName("hola");
        mockMvc.perform(post(urlTemplate, place).contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(place)))
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andDo(document("error-example",
                        responseFields(
                                fieldWithPath("error").description("The HTTP error that occurred, e.g. `Bad Request`"),
                                fieldWithPath("message").description("A description of the cause of the error"),
                                fieldWithPath("path").description("The path to which the request was made"),
                                fieldWithPath("status").description("The HTTP status code, e.g. `400`"),
                                fieldWithPath("timestamp").description("The time, in milliseconds, at which the error occurred"))));
    }

    public static LinksSnippet links(LinkDescriptor... descriptors) {
        return HypermediaDocumentation.links(
                linkWithRel("self").ignored().optional(),
                linkWithRel("place").ignored().optional());
    }

}
