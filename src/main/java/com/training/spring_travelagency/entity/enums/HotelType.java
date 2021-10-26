package com.training.spring_travelagency.entity.enums;

public enum HotelType {
    HOTEL("Hotel"),
    HOSTEL("Hostel"),
    RESORT("Resort");

    private final String name;

    HotelType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
