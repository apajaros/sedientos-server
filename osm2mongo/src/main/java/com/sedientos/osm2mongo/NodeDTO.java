package com.sedientos.osm2mongo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name="node")
public class NodeDTO {

    private long id;
    private BigDecimal lat;
    private BigDecimal lon;
    private int osmVersion;

    private List<Tag> tags;

    public NodeDTO() {
    }

//    public NodeDTO(long id, BigDecimal lat, BigDecimal lon, Collection<Tag> tags) {
//        this.id = id;
//        this.lat = lat;
//        this.lon = lon;
//        this.tags = tags;
//    }
/*
    private String name;
    private String city;
    private String street;
    private String houseNumber;
    private String postalCode;
    private boolean wheelchair;
    private String wheelchairDescription;
    private String phone;
    private String website;
    private String email;
    private String openingHours;
    private int version;*/


/*
    <tag k="addr:city" v="Madrid"/>
    <tag k="addr:housenumber" v="95"/>
    <tag k="addr:street" v="Fuencarral"/>
    <tag k="amenity" v="pub"/>
    <tag k="name" v="Sidrería la Camocha"/>
    <tag k="addr:postcode" v="28004"/>
    <tag k="phone" v="+34 913106695"/>
    <tag k="website" v="http://www.olelola.com/"/>
    <tag k="email" v="hola@murcafe.com"/>
    <tag k="contact:facebook" v="https://es-la.facebook.com/pages/ANTICAFE/56452695938"/>
    <tag k="contact:phone" v="+34 915 41 76 57"/>
    <tag k="opening_hours" v="Mo-Su 09:30-22:00"/>

    */

    public long getId() {
        return id;
    }
    @XmlAttribute
    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getLat() {
        return lat;
    }
    @XmlAttribute
    public void setLat(BigDecimal lat) {
        this.lat = lat;
    }

    public BigDecimal getLon() {
        return lon;
    }
    @XmlAttribute
    public void setLon(BigDecimal lon) {
        this.lon = lon;
    }

    public List<Tag> getTags() {
        return tags;
    }

    @XmlElement(name = "tag")
    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public int getOsmVersion() {
        return osmVersion;
    }

    @XmlAttribute
    public void setOsmVersion(int osmVersion) {
        this.osmVersion = osmVersion;
    }

    @Override
    public String toString() {
        return "NodeDTO{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", osmVersion=" + osmVersion +
                ", tags=" + tags +
                '}';
    }
}
