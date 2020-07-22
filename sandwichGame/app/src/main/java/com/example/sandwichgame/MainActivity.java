package com.example.sandwichgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    Button btnLank;
    Button btnStartGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLank= findViewById(R.id.main_btn_lank);
        btnStartGame = findViewById(R.id.main_btn_game);
        btnLank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LankActivity.class);
                startActivity(intent);
            }
        });

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
    }
}
