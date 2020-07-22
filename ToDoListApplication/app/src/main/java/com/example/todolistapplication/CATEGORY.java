package com.example.todolistapplication;

public enum CATEGORY {
    STUDY("공부"),
    CLEAN("청소"),
    PLAN("약속");
    private String type;

    CATEGORY(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }
    public static CATEGORY codeValueOf (String type) {
        for(CATEGORY category : CATEGORY.values()) {
            if(category.getType().equals(type)) {
                return category;
            }
        }
        return null;
    }

}
