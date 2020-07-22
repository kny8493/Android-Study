package com.example.todolistapplication;

import android.text.Editable;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class TodoData {
    private String index;
    private int imageView;
    private String title;
    private String status;
    private String date;
    private String memo;
    private String priority;

    public TodoData () {}

    public void setIndex(String index) {
        this.index = index;
    }
    public String getIndex() {
        return this.index;
    }

    public TodoData(String index, int imageView, String title, String status, String date, String priority, String memo) {
        this.index = index;
        this.imageView = imageView;
        this.title = title;
        this.status = status;
        this.date = date;
        this.memo = memo;
        this.priority = priority;
    }


    public TodoData(int imageView, String title, String status, String date, String priority, String memo) {
        this.imageView = imageView;
        this.title = title;
        this.status = status;
        this.date = date;
        this.memo = memo;
        this.priority = priority;
    }


    public int getImageView() {
        return imageView;
    }

    public void setImageView(int imageView) {
        this.imageView = imageView;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getMemo() { return memo; }

    public void setMemo(String addMemo) {
        this.memo = memo;
    }
}

