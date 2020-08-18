package com.example.sandwichgame;

public enum Sandwich {
    SANDWICH_A ("기본 샌드위치"),
    SANDWICH_B ("업그레이드 샌드위치"),
    SANDWICH_C ("스패셜 샌드위치");
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    Sandwich(String type) {
        this.type=type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
