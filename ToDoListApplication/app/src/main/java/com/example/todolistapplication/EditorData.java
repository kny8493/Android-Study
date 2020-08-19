package com.example.todolistapplication;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditorData {
    private String title;
    private Spinner category;
    private DatePicker date;
    private Spinner priority;
    private String memo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Spinner getCategory() {
        return category;
    }

    public void setCategory(Spinner category) {
        this.category = category;
    }

    public DatePicker getDate() {
        return date;
    }

    public void setDate(DatePicker date) {
        this.date = date;
    }

    public Spinner getPriority() {
        return priority;
    }

    public void setPriority(Spinner priority) {
        this.priority = priority;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public EditorData(String title, Spinner category, DatePicker date, Spinner priority, String memo) {
        this.title = title;
        this.category = category;
        this.date = date;
        this.priority = priority;
        this.memo = memo;
    }


}
