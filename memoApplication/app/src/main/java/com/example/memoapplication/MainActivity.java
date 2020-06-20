package com.example.memoapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MainActivity extends AppCompatActivity {
    // constant 단축키 ctrl, alt, p
    // hardcoding 비추
    public static final String FILE = "file";
    public static final String MEMO_LIST = "memoList";

    static SharedPreferences sharedPref = null;
    static SharedPreferences.Editor editor = null;

    private Button button;
    private EditText editText;
    private ArrayList<SampleData> memoList;
    private ListAdapter listAdapter;
    private ListView listView;

    // 데이터 초기화 할떄 좋은 훅인가..? -> 의문
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText)findViewById(R.id.content);
        button = (Button)findViewById(R.id.complete);
        listView = (ListView) findViewById(R.id.memo);

        // 네입 파일값 찾음
        sharedPref = getSharedPreferences(FILE, MODE_PRIVATE);
        // 데이터 기록을 위한 editor 인스턴스
        editor=sharedPref.edit();

        // 리스트 뷰 참조 , 어뎁터 달기
        listAdapter = new ListAdapter();
        listView.setAdapter(listAdapter);
    }

    // 사용자에게 보여지는 activity가 준비가 되었을때 필요한 리소스 설정
    // 여기서는 다이알로그 값들 설정하면 되는건가.. ?
    // 주로 여기서 값세팅?
    @Override
    protected void onStart(){
        super.onStart();

        getTextView();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCompleteDialog();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "일시정지", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getTextView();
        Toast.makeText(getApplicationContext(), "앱실행", Toast.LENGTH_SHORT).show();
    }
    //앱을 삭제하면 editText memo날라감
    @Override
    protected void onStop(){
        super.onStop();
        editText.setText("");
        memoList.clear();
       Toast.makeText(getApplicationContext(), "앱종료", Toast.LENGTH_SHORT).show();
    }

    // 화면에 보이지 않았다가 다시 화면에 보여야 할떄
    @Override
    protected void onRestart(){
        super.onRestart();
        Toast.makeText(getApplicationContext(), "앱재실행", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        editor.clear().commit();
        Toast.makeText(getApplicationContext(), "데이터 초기화", Toast.LENGTH_SHORT).show();

    }

    public void getTextView(){
        memoList = new ArrayList<SampleData>();

        Map<String, ?> totalItems=sharedPref.getAll();
        for(Map.Entry<String, ?> item: totalItems.entrySet()){
            memoList.add(new SampleData(item.getValue().toString()));
        }
    }

    public void openCompleteDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메모 저장");
        builder.setMessage("메모를 저장하겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // share 에도 저장하면서 list에도 add를 해야하는가 둘이 한번에 하는 방법은 없는가?
                        editor.putString(editText.getText().toString(), editText.getText().toString()).commit();
                        listAdapter.addItem(editText.getText().toString());

                        // 변경 알림
                        listAdapter.notifyDataSetChanged();

                        editText.getText().clear();

                        Toast.makeText(getApplicationContext(), R.string.complete, Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("아니요",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editText.getText().clear();
                        Toast.makeText(getApplicationContext(), R.string.cancel, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();

    }
}
