package com.example.sandwichgame;

public enum Bread {
    PLAIN_BREAD("식빵"),
    WHEAT_BREAD("밀");

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    Bread(String type) {
        this.type=type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
