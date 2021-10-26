package com.training.spring_travelagency.entity.enums;

public enum TourType {
    VACATION("Vacation"),
    EXCURSION("Excursion"),
    SHOPPING("Shopping");

    private final String name;

    TourType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
