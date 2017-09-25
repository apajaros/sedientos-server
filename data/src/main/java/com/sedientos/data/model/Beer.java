package com.sedientos.data.model;

import java.math.BigDecimal;
import java.math.MathContext;

public class Beer {
    
    private static final MathContext mathContext = new MathContext(2);

    private GlassSize glassSize;
    private BigDecimal price;

    public Beer(GlassSize glassSize, BigDecimal price) {
        this.glassSize = glassSize;
        setPrice(price);
    }

    public Beer(GlassSize pint, double v) {
        this(pint, new BigDecimal(v, mathContext));
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = new BigDecimal(price.doubleValue(), mathContext);
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
