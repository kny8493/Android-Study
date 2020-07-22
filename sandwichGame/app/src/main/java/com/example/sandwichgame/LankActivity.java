package com.example.sandwichgame;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiresApi(api = Build.VERSION_CODES.N)
public class LankActivity extends AppCompatActivity {

    private SharedPreferences sharedPref = null;
    private SharedPreferences.Editor editor = null;

    ImageButton btnMain;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerViewAdapter recyclerViewAdapter;

    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lank);

        sharedPref = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPref.edit();

        btnMain = findViewById(R.id.lank_main);
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });


        recyclerView = findViewById(R.id.lank_list);
        linearLayoutManager = new LinearLayoutManager(this);

        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
        recyclerView.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
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
        if (Optional.ofNullable(intent.getStringExtra("name")).isPresent()) {

            String name = intent.getStringExtra("name");
            String score = intent.getStringExtra("score");
            RecycleViewItem item = new RecycleViewItem(name, score);

            ArrayList<RecycleViewItem> arrayList = new ArrayList<RecycleViewItem>();
            String json = sharedPref.getString("data", null);

            // new  생성후 데이터 넣기
            if (json == null) {
                editor.putString("data", gson.toJson(arrayList)).commit();
            } else {
                Type listType = new TypeToken<ArrayList<RecycleViewItem>>() {
                }.getType();

                ArrayList<RecycleViewItem> datas = gson.fromJson(json, listType);

                datas.add(item);
                String strJson = gson.toJson(datas, listType);
                editor.putString("data", strJson).commit();
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        Log.d("RequestCOde", requestCode + "::");
        switch (requestCode) {
            case 1001: {
                //수신성공 출력
                String result = data.getStringExtra("name");
                Log.d("name", result);
            }
        }
    }
}
