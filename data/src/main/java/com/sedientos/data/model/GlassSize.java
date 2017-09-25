package com.sedientos.data.model;

public enum GlassSize {
    SMALL("small"),
    DOUBLE("double"),
    PINT("pint");

    private String name;

    GlassSize(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
