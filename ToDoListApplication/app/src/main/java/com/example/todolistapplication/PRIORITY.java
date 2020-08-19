package com.example.todolistapplication;

public enum PRIORITY {
    HIGH("높음"),
    NORMAL("일반"),
    LOW("낮음");

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    PRIORITY(String type) {
        this.type=type;
    }

    @Override
    public String toString() {
        return this.type;
    }
}
