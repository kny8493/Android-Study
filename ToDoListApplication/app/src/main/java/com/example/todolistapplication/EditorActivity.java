package com.example.todolistapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.borax12.materialdaterangepicker.date.DatePickerDialog;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.N)
public class EditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    public static EditorActivity activity = null;
    private static final String KEY_DATA = "KEY_DATA";
    static SharedPreferences sharedPref = null;
    static SharedPreferences.Editor editor = null;

    private Spinner category;
    private EditText title;
    private Spinner priority;
    private EditText memo;
    private TextView dateTextView;

    // top button
    private Button saveButton;
    private Button gotoListButton;

    private int index;
    private AlertDialog.Builder builder;

    String modifyIndex;
    SimpleDateFormat dateformat;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.todo_editor);

        Intent intent = getIntent();

        dateformat = new SimpleDateFormat("yyyy-MM-dd");

        dateTextView = (TextView)findViewById(R.id.date_textview);
        title = (EditText) findViewById(R.id.todo_title_content);
        category = (Spinner) findViewById(R.id.todo_category_data);
        Button dateButton = (Button)findViewById(R.id.date_button);
        priority = (Spinner) findViewById(R.id.todo_priority_spinner);
        memo = (EditText) findViewById(R.id.todo_memo_data);

        saveButton = (Button) findViewById(R.id.editor_save_button);
        gotoListButton = (Button) findViewById(R.id.godo_list_button);

        sharedPref = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPref.edit();

        activity = this;

        builder = new AlertDialog.Builder(this);

        // sharedPre의 값가져옴
        index = sharedPref.getAll().size();

        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = com.borax12.materialdaterangepicker.date.DatePickerDialog.newInstance(
                        EditorActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );;
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        if(saveInstanceState!=null){
            String text = saveInstanceState.getString(KEY_DATA);
            title.setText(text);

            builder.setMessage("이전에 작성하던 글이 있습니다. 이어서 하겠습니까?");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "이어서 작성", Toast.LENGTH_SHORT).show();
                        }
                    });
            builder.setNegativeButton("아니요",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            onStart();
                        }
                    });
            builder.show();
        }


        if(intent.getBooleanExtra("modify", false)) {
            modifyIndex = sharedPref.getString("index", "-1");
            if(!modifyIndex.equals("-1")) {
                String mData = sharedPref.getString(modifyIndex, " ");
                TodoData todoData = gson.fromJson(mData, TodoData.class);
                title.setText(todoData.getTitle());
                memo.setText(todoData.getMemo());
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        List<CATEGORY> categories = new ArrayList<CATEGORY>(EnumSet.allOf(CATEGORY.class));
        ArrayAdapter<CATEGORY> categoryArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(categoryArrayAdapter);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCompleteDialog();
            }
        });

        List<PRIORITY> priorities = new ArrayList<PRIORITY>(EnumSet.allOf(PRIORITY.class));
        ArrayAdapter<PRIORITY> priorityArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, priorities);
        priorityArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priority.setAdapter(priorityArrayAdapter);

        // godo list intent
        gotoListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }


    // 저장 버튼
    public void openCompleteDialog() {

        builder = new AlertDialog.Builder(this);

        builder.setTitle("저장");
        builder.setMessage("할일을 등록하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int icon = 0;

                        String selectedItem = category.getSelectedItem().toString();
                        if (selectedItem.equals(CATEGORY.CLEAN.getType())) {
                            icon = R.drawable.ic_delete_sweep_black_24dp;
                        } else if (selectedItem.equals(CATEGORY.PLAN.getType())) {
                            icon = R.drawable.ic_insert_invitation_black_24dp;
                        } else if (selectedItem.equals(CATEGORY.STUDY.getType())) {
                            icon = R.drawable.ic_edit_black_24dp;
                        } else {
                            icon = R.drawable.ic_content_copy_black_24dp;
                        }

                        Intent intent = new Intent();
                        intent.putExtra("icon", icon);
                        intent.putExtra("title", title.getText().toString());
                        intent.putExtra("date", dateTextView.getText());
                        intent.putExtra("priority", priority.getSelectedItem().toString());
                        intent.putExtra("memo", memo.getText().toString());


                        TodoData editorData = new TodoData(icon, title.getText().toString(), STATUS.WAITING.name(), dateTextView.getText().toString(), priority.getSelectedItem().toString(), memo.getText().toString());

                        modifyIndex = sharedPref.getString("index", "-1");
                        if(!modifyIndex.equals("-1")) {
                            editor.putString(modifyIndex, gson.toJson(editorData)).commit();
                        } else {

                            Map<String, ?> allEntries = sharedPref.getAll();

                            int lastIndex = 0;

                            for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                                if(entry.getKey().equals("index")) continue;
                                if(lastIndex<Integer.parseInt(entry.getKey())){
                                    lastIndex = Integer.parseInt(entry.getKey());
                                }
                            }
                            lastIndex++;

                            editor.putString(String.valueOf(lastIndex), gson.toJson(editorData));
                            editor.commit();

                        }

                        Log.d("commit", "commit");
                        setResult(RESULT_OK, intent);
                        finish();

                        Toast.makeText(getApplicationContext(), "저장성공", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("아니요",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "저장 안함", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.show();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String text = savedInstanceState.getString(KEY_DATA);
        title.setText(text);

        builder.setMessage("이전에 작성하던 글이 있습니다. 이어서 하겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "이어서 작성", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton("아니요",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onStart();
                    }
                });
        builder.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String data = title.getText().toString();
        outState.putString(KEY_DATA, data);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("life", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("life", "onStop");
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) getFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }
    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth, int yearEnd, int monthOfYearEnd, int dayOfMonthEnd) {
        String date =year+"/"+(++monthOfYear)+"/"+dayOfMonth+" ~ "+yearEnd+"/"+dayOfMonthEnd+"/"+(++monthOfYearEnd);
        dateTextView.setText(date);
    }
}
