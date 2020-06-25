package com.example.memoapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
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

    static SharedPreferences sharedPref = null;
    static SharedPreferences.Editor editor = null;

    private Button completeBtn;
    private Button listBtn;
    private EditText editText;

    // 데이터 초기화 할떄 좋은 훅인가..? -> 의문
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.content);
        completeBtn = (Button) findViewById(R.id.complete);

        // 네입 파일값 찾음
        sharedPref = getSharedPreferences(FILE, MODE_PRIVATE);
        // 데이터 기록을 위한 editor 인스턴스
        editor = sharedPref.edit();


        // 메뉴로 가는 intent 등록
        listBtn = findViewById(R.id.go_list);
        listBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ListActivity.class);
                intent.putExtra("MESSAGE", "목록으로 화면전환");
                startActivity(intent);
            }
        });
    }

    // 사용자에게 보여지는 activity가 준비가 되었을때 필요한 리소스 설정
    // 여기서는 다이알로그 값들 설정하면 되는건가.. ?
    // 주로 여기서 값세팅?
    @Override
    protected void onStart() {
        super.onStart();
        completeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCompleteDialog();
            }
        });
    }

    // 앱에서 반쯤 안보일떄
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(getApplicationContext(), "일시정지", Toast.LENGTH_SHORT).show();
    }

    // onPause다음에 호출되는것
    @Override
    protected void onResume() {
        super.onResume();
        // 화면 전환 intent
        Intent getMemoIntent = getIntent();
        if (getMemoIntent != null) {
            Toast.makeText(getApplicationContext(), "새 메모 작성", Toast.LENGTH_SHORT).show();
        }
    }

    // activity 나가고 새로운 activity 켰을때
    // 앱정상 종료
   @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "데이터 초기화", Toast.LENGTH_SHORT).show();
    }

    public void openCompleteDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("메모 저장");
        builder.setMessage("메모를 저장하겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString(editText.getText().toString(), editText.getText().toString()).commit();
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
