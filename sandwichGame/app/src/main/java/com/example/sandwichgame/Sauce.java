package com.example.sandwichgame;

public enum Sauce {
    MAYONNAISE ("마요네즈"),
    SPICY_SAUCE("캡사이신"),
    MUSTARD("머스타드"),
    TOMATO_SAUCE("케첩");

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    Sauce(String type) {
        this.type=type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
