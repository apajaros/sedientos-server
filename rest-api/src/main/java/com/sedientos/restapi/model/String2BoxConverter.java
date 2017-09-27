package com.sedientos.restapi.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class String2BoxConverter implements Converter<String, Box> {
    private static final Log LOGGER =  LogFactory.getLog(String2BoxConverter.class.getName());

    @Override
    public Box convert(String s) {
        Assert.notNull(s, "String can't be null");
        String[] coordinates = s.split(",");
        Assert.isTrue(coordinates.length == 4, "A Box must be defined by four coordinates");
        Point first = new Point(Double.valueOf(coordinates[0]), Double.valueOf(coordinates[1]));
        Point second = new Point(Double.valueOf(coordinates[2]), Double.valueOf(coordinates[3]));
        return new Box(first, second);
    }
}
