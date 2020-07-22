package com.example.sandwichgame;

import android.widget.TextView;

public class RecycleViewItem implements Comparable<RecycleViewItem>{
    private String number;
    private String name;
    private String score;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
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

    public RecycleViewItem(String number, String name, String score) {

        this.number = number;
        this.name = name;
        this.score = score;
    }
    public RecycleViewItem(String name, String score) {
        this.name = name;
        this.score = score;
    }


    @Override
    public int compareTo(RecycleViewItem item) {
        return item.score.compareTo(this.score);
    }
}
