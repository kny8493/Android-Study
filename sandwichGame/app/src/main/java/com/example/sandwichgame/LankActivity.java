package com.example.sandwichgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiresApi(api = Build.VERSION_CODES.N)
public class LankActivity extends AppCompatActivity {

    private SharedPreferences sharedPref = null;
    private SharedPreferences.Editor editor = null;

    ImageButton btnMain;
    ImageButton btnReplay;

    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;

    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lank);

        Intent intent = getIntent();

        sharedPref = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPref.edit();

        btnMain = findViewById(R.id.lank_main);
        btnReplay = findViewById(R.id.lank_game_replay);

        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
                finish();
            }
        });


        recyclerView = findViewById(R.id.lank_list);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);

        getScoreData(intent);
    }

    @Override
    protected void onStart() {

        super.onStart();

        List<RecycleViewItem> datas = getData();

        recyclerViewAdapter = new RecyclerViewAdapter(this, datas);
        recyclerView.setAdapter(recyclerViewAdapter);
    }


    public List<RecycleViewItem> getData() {

        gson = new GsonBuilder().create();

        String json = sharedPref.getString("data", "");
        Type listType = new TypeToken<ArrayList<RecycleViewItem>>() {
        }.getType();

        List<RecycleViewItem> datas = gson.fromJson(json, listType);

        Collections.sort(datas);

        for (int i = 0; i < datas.size(); i++) {
            RecycleViewItem item = datas.get(i);
            item.setNumber(String.valueOf(i + 1));
        }

        return datas;
    }

    public void getScoreData(Intent intent) {
        Gson gson = new Gson();
        if (Optional.ofNullable(intent.getExtras()).isPresent()) {

            String name = intent.getExtras().get("name").toString();
            String score = intent.getExtras().get("score").toString();
            String imageUrl = intent.getExtras().get("image").toString();
            RecycleViewItem item = new RecycleViewItem(name, score, imageUrl);

            ArrayList<RecycleViewItem> arrayList = new ArrayList<RecycleViewItem>();
            String json = sharedPref.getString("data", "");

            // new  생성후 데이터 넣기
            if (json.length() == 0) {
                editor.putString("data", gson.toJson(arrayList)).commit();
            } else {
                Type listType = new TypeToken<ArrayList<RecycleViewItem>>() {
                }.getType();

                ArrayList<RecycleViewItem> datas = gson.fromJson(json, listType);

                datas.add(item);
                String strJson = gson.toJson(datas, listType);
                editor.putString("data", strJson).commit();
            }
        } else {
            ArrayList<RecycleViewItem> arrayList = new ArrayList<RecycleViewItem>();
            String json = sharedPref.getString("data", "");

            // new  생성후 데이터 넣기
            if (json.length() == 0) {
                editor.putString("data", gson.toJson(arrayList)).commit();
            } else {
                Type listType = new TypeToken<ArrayList<RecycleViewItem>>() {
                }.getType();

                ArrayList<RecycleViewItem> datas = gson.fromJson(json, listType);

                String strJson = gson.toJson(datas, listType);
                editor.putString("data", strJson).commit();
            }
        }
    }
}
