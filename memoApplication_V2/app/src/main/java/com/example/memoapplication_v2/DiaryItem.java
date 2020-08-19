package com.example.memoapplication_v2;

import android.view.View;

import com.google.auto.value.AutoValue;

public class DiaryItem {
    String title;
    String date;
    String imageUri;
    String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DiaryItem() {}

    public DiaryItem(String title, String date, String imageUri, String content) {
        this.title = title;
        this.date = date;
        this.imageUri = imageUri;
        this.content = content;
    }

    public DiaryItem(String title, String date, String content) {
        this.title = title;
        this.date = date;
        this.content = content;
    }

}
