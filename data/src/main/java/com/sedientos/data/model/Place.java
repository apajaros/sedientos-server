package com.sedientos.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "places")
public class Place {

    @Id
    private String id;

    @NotEmpty
    @NotNull
    @TextIndexed
    private String name;
    // TODO: We should have a proper address model (street type, name, number, postal code, city, country)
    @NotEmpty
    @NotNull
    @TextIndexed
    private String address;
    @NotNull
    @JsonSerialize(using = PointSerializer.class)
    @GeoSpatialIndexed
    private Point location;
    private List<Beer> beers;
    @Indexed(sparse = true)
    private BigDecimal stars;
    private List<Review> reviews;
    private String phoneNumber;
    private String website;
    private String email;
    private String openingHours;
    private String street;
    private String houseNumber;
    private String city;
    private String postalCode;
    /**
     * Open Street Maps ID
     */
    private long osmId;
    private int osmVersion;

    public Place() {}

    public Place(String id, String name, String address, Point location, List<Beer> beers, BigDecimal stars, List<Review> reviews) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.location = location;
        this.beers = beers;
        this.stars = stars;
        this.reviews = reviews;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public List<Beer> getBeers() {
        return beers;
    }

    public void setBeers(List<Beer> beers) {
        this.beers = beers;
    }

    public void addBeer(Beer beer) {
         if (this.beers == null) {
             this.beers = new ArrayList<>();
         }
         beers.add(beer);
    }

    public BigDecimal getStars() {
        return stars;
    }

    public void setStars(BigDecimal stars) {
        this.stars = stars;
    }

    public List<Review> getReviews() {
        if (this.reviews == null) {
            this.reviews = new ArrayList<>();
        }
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    public void addReview(Review review) {
        this.getReviews().add(review);
    }

    @Override
    public String toString() {
        return "Place{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", location=" + location +
                ", beers=" + beers +
                ", stars=" + stars +
                ", reviews=" + reviews +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Place place = (Place) o;

        if (!id.equals(place.id)) return false;
        if (!name.equals(place.name)) return false;
        if (!address.equals(place.address)) return false;
        if (!location.equals(place.location)) return false;
        if (beers != null ? !beers.equals(place.beers) : place.beers != null) return false;
        if (stars != null ? !stars.equals(place.stars) : place.stars != null) return false;
        return reviews != null ? reviews.equals(place.reviews) : place.reviews == null;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + address.hashCode();
        result = 31 * result + location.hashCode();
        result = 31 * result + (beers != null ? beers.hashCode() : 0);
        result = 31 * result + (stars != null ? stars.hashCode() : 0);
        result = 31 * result + (reviews != null ? reviews.hashCode() : 0);
        return result;
    }

    public long getOsmId() {
        return osmId;
    }

    public void setOsmId(long osmId) {
        this.osmId = osmId;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getWebsite() {
        return website;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOsmVersion(int osmVersion) {
        this.osmVersion = osmVersion;
    }

    public int getOsmVersion() {
        return osmVersion;
    }

    public String getId() {
        return id;
    }
}
