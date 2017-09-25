package com.sedientos.data.model;

import java.math.BigDecimal;

public class Beer {

    private GlassSize glassSize;
    private BigDecimal price;

    public Beer(GlassSize glassSize, BigDecimal price) {
        this.glassSize = glassSize;
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public GlassSize getGlassSize() {
        return glassSize;
    }

    public void setGlassSize(GlassSize glassSize) {
        this.glassSize = glassSize;
    }

    @Override
    public String toString() {
        return "Beer{" +
                "glassSize=" + glassSize +
                ", price=" + price +
                '}';
    }
}
