package com.sedientos.restapi.model;

import org.junit.Test;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;

import static org.junit.Assert.*;

public class String2BoxConverterTest {
    @Test
    public void convertNull() {
        String2BoxConverter converter = new String2BoxConverter();
        String s = null;
        try {
            converter.convert(s);
            fail("Should throw an exception on null argument");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void convertEmpty() {
        String2BoxConverter converter = new String2BoxConverter();
        String s = "";
        try {
            converter.convert(s);
            fail("Should throw an exception on empty argument");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void convertThreePoints() {
        String2BoxConverter converter = new String2BoxConverter();
        String s = "1,2,3";
        try {
            converter.convert(s);
            fail("Should throw an exception on wrong number of coordinates in argument");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void convertFivePoints() {
        String2BoxConverter converter = new String2BoxConverter();
        String s = "1,2,3,4,5";
        try {
            converter.convert(s);
            fail("Should throw an exception on wrong number of coordinates in argument");
        } catch (IllegalArgumentException e) {
            // Expected exception thrown
        }
    }

    @Test
    public void convertFourPoints() {
        String2BoxConverter converter = new String2BoxConverter();
        String s = "1,2,3,4";
        try {
            Box box = converter.convert(s);
            Point firstPoint = box.getFirst();
            assertEquals(1, firstPoint.getX(), 0);
            assertEquals(2, firstPoint.getY(), 0);
            Point secondPoint = box.getSecond();
            assertEquals(3, secondPoint.getX(), 0);
            assertEquals(4, secondPoint.getY(), 0);
        } catch (IllegalArgumentException e) {
            fail("Should throw an exception on wrong number of coordinates in argument");
        }
    }
}