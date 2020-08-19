package com.example.memoapplication_v2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewActivity extends AppCompatActivity {


    private SharedPreferences sharedPref = null;
    private SharedPreferences.Editor editor = null;

    CalendarView calendar;
    TextView date;
    TextView imageUpload;
    ImageButton image;
    ImageView imagePreview;
    ImageButton save;
    ImageView gotoList;
    EditText title;
    EditText content;

    boolean isShowCalender;
    boolean isShowImage;

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.M.d");

    StorageReference storageRef;
    Uri filePath;
    String filename;

    Boolean isUploadFile = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getSharedPreferences("data", MODE_PRIVATE);
        editor = sharedPref.edit();

        setContentView(R.layout.new_memo);

        date = findViewById(R.id.new_date_data);
        date.setText(dateFormat.format(new Date()));

        calendar = findViewById(R.id.new_date_calender);
        isShowCalender = false;
        isShowImage = false;

        calendar.setVisibility(View.GONE);

        imageUpload = findViewById(R.id.new_image_upload);
        image = findViewById(R.id.new_image_show);
        imagePreview = findViewById(R.id.new_image_preview);
        imagePreview.setVisibility(View.GONE);
        gotoList = findViewById(R.id.new_goto_list);
        save = findViewById(R.id.new_save);
        title = findViewById(R.id.new_title);
        content = findViewById(R.id.new_content);

        getIntent();
    }

    @Override
    protected void onStart() {
        super.onStart();

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalender();
            }
        });

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                date.setText(year + "." + (month + 1) + "." + dayOfMonth);
            }
        });


        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 이미지 선택
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "이미지를 선택하세요."), 0);

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
                DiaryItem item = new DiaryItem();

                if(isUploadFile) {
                    item.setContent(content.getText().toString());
                    item.setImageUri(filename);
                    item.setDate(date.getText().toString());
                    item.setTitle(title.getText().toString());
                } else {
                    item.setImageUri("");
                    item.setContent(content.getText().toString());
                    item.setDate(date.getText().toString());
                    item.setTitle(title.getText().toString());
                }

                String data = sharedPref.getString("data", "");

                Gson gson = new Gson();
                if (data.isEmpty()) {
                    List<DiaryItem> diaryItems = new ArrayList<>();
                    diaryItems.add(item);
                    editor.putString("data", gson.toJson(diaryItems)).commit();
                } else {
                    Type listType = new TypeToken<ArrayList<DiaryItem>>() {
                    }.getType();
                    List<DiaryItem> diaryItems = gson.fromJson(data, listType);
                    diaryItems.add(item);
                    editor.putString("data", gson.toJson(diaryItems)).commit();
                }
                initData();
            }
        });

        gotoList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // 카메라에서 파일 가져오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //request코드가 0이고 OK를 선택했고 data에 뭔가가 들어 있다면
        if (requestCode == 0 && resultCode == RESULT_OK) {
            filePath = data.getData();
            try {
                //Uri 파일을 Bitmap으로 만들어서 ImageView에 집어 넣는다.
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imagePreview.setImageBitmap(bitmap);
                isUploadFile = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //upload the file
    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null && isUploadFile) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMHH_mmss");
            Date now = new Date();
            filename = formatter.format(now);
            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://memoapplicationv2.appspot.com").child("images/" + filename);

            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            isUploadFile = false;
                            Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            isUploadFile = false;
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            if (progressDialog != null) {
                                @SuppressWarnings("VisibleForTests")
                                double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                //dialog에 진행률을 퍼센트로 출력해 준다
                                progressDialog.setMessage(((int) progress) + "% ...");
                            }
                        }
                    });
        } else {
            isUploadFile = false;
            Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCalender() {
        if (isShowCalender) {

            TranslateAnimation animation = new TranslateAnimation(
                    0,
                    0,
                    0,
                    calendar.getHeight());

            animation.setDuration(500);
            animation.setFillAfter(true);
            calendar.startAnimation(animation);
            calendar.setVisibility(View.GONE);
        } else {
            // 애니메이션이 처음 적용 안되는 이유?
            TranslateAnimation animation = new TranslateAnimation(
                    0,
                    0,
                    -calendar.getHeight(),
                    0);
            animation.setDuration(500);
            animation.setFillAfter(true);

            calendar.startAnimation(animation);
            calendar.setVisibility(View.VISIBLE);
        }
        isShowCalender = !isShowCalender;
    }

    private void showImage() {

        if (isShowImage) {
            TranslateAnimation animation = new TranslateAnimation(
                    0,
                    0,
                    0,
                    imagePreview.getHeight());

            animation.setDuration(500);
            animation.setFillAfter(true);
            imagePreview.startAnimation(animation);
            imagePreview.setVisibility(View.GONE);
            image.setImageResource(R.drawable.ic_down_22dp);
        } else {
            TranslateAnimation animation = new TranslateAnimation(
                    0,
                    0,
                    -imagePreview.getHeight(),
                    0);
            animation.setDuration(500);
            animation.setFillAfter(true);

            imagePreview.startAnimation(animation);
            imagePreview.setVisibility(View.VISIBLE);
            image.setImageResource(R.drawable.ic_up_22dp);
        }
        isShowImage = !isShowImage;
    }

    private void initData() {
        content.setText(null);
        date.setText(dateFormat.format(new Date()));
        imagePreview.setImageResource(0);
        title.setText(null);
        isUploadFile = false;
    }
}
