package com.sedientos.data.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BeerTest {
    @Test
    public void priceHasTwoDigitsPrecision() throws Exception {
        Beer beer = new Beer(GlassSize.SMALL, 0.12345678);
        assertEquals("0.12", beer.getPrice().toString());
    }

    @Test
    public void priceRoundsHalfUp() throws Exception {
        Beer beer = new Beer(GlassSize.SMALL, 0.128);
        assertEquals("0.13", beer.getPrice().toString());
    }

}