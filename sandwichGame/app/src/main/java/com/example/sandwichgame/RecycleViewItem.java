package com.example.sandwichgame;

import android.net.Uri;

public class RecycleViewItem implements Comparable<RecycleViewItem> {
    private String number;
    private String name;
    private String score;
    private String imageUrl;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public RecycleViewItem(String number, String name, String score, String imageUrl) {

        this.number = number;
        this.name = name;
        this.score = score;
        this.imageUrl = imageUrl;
    }

    public RecycleViewItem(String name, String score, String imageUrl) {
        this.name = name;
        this.score = score;
        this.imageUrl = imageUrl;
    }

    @Override
    public int compareTo(RecycleViewItem item) {
        return Integer.parseInt(item.getScore()) - Integer.parseInt(getScore());
    }
}
