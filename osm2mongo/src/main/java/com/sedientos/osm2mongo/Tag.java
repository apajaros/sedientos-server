package com.sedientos.osm2mongo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="tag")
public class Tag {

    private String k;
    private String v;

    public String getK() {
        return k;
    }

    @XmlAttribute
    public void setK(String k) {
        this.k = k;
    }

    public String getV() {
        return v;
    }

    @XmlAttribute
    public void setV(String v) {
        this.v = v;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "k='" + k + '\'' +
                ", v='" + v + '\'' +
                '}';
    }
}
