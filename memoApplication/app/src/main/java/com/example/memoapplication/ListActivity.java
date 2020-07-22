package com.example.memoapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;

public class ListActivity extends Activity {

    public static final String FILE = "file";

    static SharedPreferences sharedPref = null;
    static SharedPreferences.Editor editor = null;

    private ImageButton addMemoBtn;

    private ArrayList<SampleData> memoList;
    private ListAdapter listAdapter;
    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memo_list);

        final Intent intent = getIntent();
        String message = intent.getStringExtra("MESSAGE");

        listView = (ListView) findViewById(R.id.list);
        addMemoBtn = (ImageButton) findViewById(R.id.new_memo);
        addMemoBtn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sendMemoIntent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(sendMemoIntent);
                    }
                }
        );

        // 네입 파일값 찾음
        sharedPref = getSharedPreferences(FILE, MODE_PRIVATE);

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart(){
        super.onStart();
        // 리스트를 가져오고
        getTextView();

        // list adapter에 가져온 리스트 set
        listAdapter = new ListAdapter(memoList);
        listView.setAdapter(listAdapter);
    }

    public void getTextView(){
        memoList = new ArrayList<SampleData>();

        Map<String, ?> totalItems=sharedPref.getAll();
        for(Map.Entry<String, ?> item: totalItems.entrySet()){
            memoList.add(new SampleData(item.getValue().toString()));
        }
    }
}
