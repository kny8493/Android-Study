package com.example.memoapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle intentState){
        super.onCreate(intentState);
        setContentView(R.layout.preview_dialog);

        Intent intent = new Intent(this.getIntent());
        String content = intent.getStringExtra("content");

        TextView textView = (TextView)findViewById(R.id.preview_content);
        textView.setText(content);

    }
}
