package com.example.sandwichgame;

public enum Vegetable {
    CABbAGE ("양배추"),
    TOMATO ("토마토"),
    ONION("양파");
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    Vegetable(String type) {
        this.type=type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
