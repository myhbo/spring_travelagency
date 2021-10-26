package com.training.spring_travelagency.entity.enums;

public enum OrderStatus {
    PROCESSING("Processing"),
    CONFIRMED("Confirmed"),
    REJECTED("Rejected");

    private final String name;

    OrderStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
