package com.example.todolistapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.kyleduo.switchbutton.SwitchButton;

import java.lang.reflect.Array;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {
    // new editor activity request code
    static final int sub = 1001;
    static final int MODIFY = 1002;

    String[] types = {"수정", "삭제"};

    private ImageButton newTodo;

    ArrayList<TodoData> todoDataArrayList = new ArrayList<TodoData>();
    SwitchButton todoStatus;
    ListView listView;
    Gson gson = new Gson();
    PullRefreshLayout loading;

    private SharedPreferences sharedPref = null;
    private SharedPreferences.Editor editor = null;
    TodoAdapter adapter;
    int checkPoint;

    private AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPref = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPref.edit();

        loading = (PullRefreshLayout)findViewById(R.id.swipeRefreshLayout);
        loading.setRefreshStyle(PullRefreshLayout.STYLE_RING);
        //pullrefresh가 시작됬을 시 호출
        loading.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            //Thread - 1초 후 로딩 종료
                Handler delayHandler = new Handler();
                delayHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loading.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        this.initializeListData();

        listView = (ListView) findViewById(R.id.todo_list);

        adapter = new TodoAdapter(this, todoDataArrayList);
        listView.setAdapter(adapter);

        newTodo = (ImageButton) findViewById(R.id.new_btn);
        todoStatus = (SwitchButton) findViewById(R.id.todo_status);
        builder = new AlertDialog.Builder(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("clicked", "pos: " + position);
                checkPoint = listView.getCheckedItemPosition();
                Object ob = (Object) parent.getAdapter().getItem(position);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                openDeleteDialog(position);
                Log.d("long clicked", "pos: " + position);
                return true;
            }
        });
    }

    public void initializeListData() {
        //
        if (Optional.ofNullable(sharedPref).isPresent()) {
            for (Map.Entry<String, ?> entry : sharedPref.getAll().entrySet()) {
                if(entry.getKey().equals("index")) continue;
                Log.d("entity", entry.getValue().toString());
                Gson gson = new Gson();
                TodoData todoData = gson.fromJson(entry.getValue().toString(), TodoData.class);
                todoData.setIndex(entry.getKey());
                todoDataArrayList.add(todoData);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(EditorActivity.activity != null) {
            EditorActivity activity = (EditorActivity)EditorActivity.activity;
            activity.finish();
        }

        editor.putString("index", "-1");
        editor.apply();
        newTodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditorActivity.class);
                // 액티비디 띄우기
                Log.d("intent", "intent");
                startActivityForResult(intent, sub);
            }
        });
    }


    public void modify(int position, TodoData todoData) {

        int count = 0;

        Map<String, ?> allEntries = sharedPref.getAll();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if(entry.getKey().equals("index")) continue;
            if(count == position){
                count = Integer.parseInt(entry.getKey());
                break;
            }
            count++;
        }
        editor.putString("index", String.valueOf(count));
        editor.commit();
        Intent intent = new Intent(getApplicationContext(),EditorActivity.class);

        intent.putExtra("modify", true);
        startActivityForResult(intent, MODIFY);

    }

    public void delete (final int position, final TodoData todoData){

        builder.setTitle("삭제");
        builder.setMessage("삭제하겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int count = 0;

                        Map<String, ?> allEntries = sharedPref.getAll();

                        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                            if(entry.getKey().equals("index")) continue;
                            if(count == position){
                                count = Integer.parseInt(entry.getKey());
                                break;
                            }
                            count++;
                        }
                        editor.remove(String.valueOf(count)).commit();
                        todoDataArrayList.remove(position);

                        adapter.notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "삭제안함", Toast.LENGTH_SHORT).show();
                    }

                    ;
                });
        builder.show();
    }

    public void openDeleteDialog(final int position) {
        
        AlertDialog.Builder typeDialog = new AlertDialog.Builder(this);

        final TodoData data = todoDataArrayList.get(position);

        typeDialog.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0 : // 수정
                        modify(position, data);
                        break;
                    case 1 : // 삭제
                        delete(position, data);
                        break;
                }
            }
        });

        typeDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1001:
                    Log.d("onActivy", "onAcitivy");

                    int icon = Objects.requireNonNull(data.getExtras().getInt("icon"));
                    String title = data.getExtras().getString("title");
                    String date = data.getExtras().getString("date");
                    String memo = data.getExtras().getString("memo");
                    String priority = data.getExtras().getString("priority");

                    adapter.addItem(icon, title, STATUS.WAITING.name(), date, priority, memo);
                    adapter.notifyDataSetChanged();
                    break;
                case 1002 :

                    initializeListData();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(), "수정완료", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
