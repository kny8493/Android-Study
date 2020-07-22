package com.example.sandwichgame;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {
    public static final int lank = 1001; // 게임 종료후 랭킹 페이지로


    Random random = new Random();//랜덤 객체 생성

    TextView gameTimer;
    ImageButton gotoMainBtn;

    TextView orderItem1;
    TextView orderItem2;
    TextView orderItem3;

    ImageButton tableCook1;
    ImageButton tableCook2;
    ImageButton tableCook3;

    TextView tableTimer1;
    TextView tableTimer2;
    TextView tableTimer3;

    Button bread1;
    Button bread2;

    Button veg1;
    Button veg2;
    Button veg3;

    Button sauce1;
    Button sauce2;
    Button sauce3;
    Button sauce4;

    ImageView table1BreadImage;
    ImageView table1VegImage;
    ImageView table1SauceImage;

    ImageView table2BreadImage;
    ImageView table2VegImage;
    ImageView table2Sauce1Image;
    ImageView table2Sauce2Image;

    ImageView table3BreadImage;
    ImageView table3Veg1Image;
    ImageView table3Veg2Image;
    ImageView table3Veg3Image;
    ImageView table3Sauce1Image;
    ImageView table3Sauce2Image;

    boolean dragFlag1 = false;
    boolean dragFlag2 = false;
    boolean dragFlag3 = false;

    int tableTimerValue1 = 20;
    int tableTimerValue2 = 20;
    int tableTimerValue3 = 20;

    Handler handler1 = new Handler();
    Timer timer1;

    Handler handler2 = new Handler();
    Timer timer2;

    Handler handler3 = new Handler();
    Timer timer3;

    int ingredientCnt1=3;
    int ingredientCnt2=4;
    int ingredientCnt3=5;

    int randItemCnt1;
    int randItemCnt2;
    int randItemCnt3;

    TextView totalScore;
    int score = 0;

    AlertDialog.Builder finshedDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);

        String conversionTime = "0045";
        countDown(conversionTime);
        gameTimer = findViewById(R.id.game_timer);
        gotoMainBtn = findViewById(R.id.goto_main);
        orderItem1 = findViewById(R.id.order_item1_number);
        orderItem2 = findViewById(R.id.order_item2_number);
        orderItem3 = findViewById(R.id.order_item3_number);

        tableCook1 = findViewById(R.id.table_1_cook);
        tableCook2 = findViewById(R.id.table_2_cook);
        tableCook3 = findViewById(R.id.table_3_cook);

        tableTimer1 = findViewById(R.id.table_1_timer);
        tableTimer2 = findViewById(R.id.table_2_timer);
        tableTimer3 = findViewById(R.id.table_3_timer);

        bread1 = findViewById(R.id.material_bread_1);
        bread2 = findViewById(R.id.material_bread_2);

        veg1 = findViewById(R.id.material_veg_1);
        veg2 = findViewById(R.id.material_veg_2);
        veg3 = findViewById(R.id.material_veg_3);

        sauce1 = findViewById(R.id.material_sauce_1);
        sauce2 = findViewById(R.id.material_sauce_2);
        sauce3 = findViewById(R.id.material_sauce_3);
        sauce4 = findViewById(R.id.material_sauce_4);

        table1BreadImage = (ImageView) findViewById(R.id.table_1_cook_bread_img);
        table1VegImage = (ImageView) findViewById(R.id.table_1_cook_veg_img);
        table1SauceImage = (ImageView) findViewById(R.id.table_1_cook_sauce_img);

        table2BreadImage = (ImageView) findViewById(R.id.table_2_cook_bread_img);
        table2VegImage = (ImageView) findViewById(R.id.table_2_cook_veg_img);
        table2Sauce1Image = (ImageView) findViewById(R.id.table_2_cook_sauce_1_img);
        table2Sauce2Image = (ImageView) findViewById(R.id.table_2_cook_sauce_2_img);

        table3BreadImage = (ImageView) findViewById(R.id.table_3_cook_bread_img);
        table3Veg1Image = (ImageView) findViewById(R.id.table_3_cook_veg_1_img);
        table3Veg2Image = (ImageView) findViewById(R.id.table_3_cook_veg_2_img);
        table3Veg3Image = (ImageView) findViewById(R.id.table_3_cook_veg_3_img);
        table3Sauce1Image = (ImageView) findViewById(R.id.table_3_cook_sauce_1_img);
        table3Sauce2Image = (ImageView) findViewById(R.id.table_3_cook_sauce_2_img);

        totalScore = findViewById(R.id.score_number);


        gotoMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    protected void onStart() {
        super.onStart();
        randItemCnt1 = random.nextInt(15)+4;
        randItemCnt2 = random.nextInt(15)+4;
        randItemCnt3 = random.nextInt(15)+4;

        orderItem1.setText(Integer.toString(randItemCnt1));
        orderItem2.setText(Integer.toString(randItemCnt2));
        orderItem3.setText(Integer.toString(randItemCnt3));

        tableTimer1.setText(String.valueOf(tableTimerValue1) + "초");
        tableTimer2.setText(String.valueOf(tableTimerValue2) + "초");
        tableTimer3.setText(String.valueOf(tableTimerValue3) + "초");

        score = Integer.parseInt(totalScore.getText().toString());


        bread1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                //롱클릭시 클립데이터를 만듬
                ClipData clip = ClipData.newPlainText("bread1", "bread1");
                //드래그할 데이터, 섀도우 지정, 드래그 앤 드롭 관련 데이터를 가지는 객체 지정, 0
                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);
                return false;
            }
        });


        bread2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                //롱클릭시 클립데이터를 만듬
                ClipData clip = ClipData.newPlainText("bread2", "bread2");
                //드래그할 데이터, 섀도우 지정, 드래그 앤 드롭 관련 데이터를 가지는 객체 지정, 0
                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);
                return false;

            }
        });

        veg1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                //롱클릭시 클립데이터를 만듬
                ClipData clip = ClipData.newPlainText("veg1", "veg1");
                //드래그할 데이터, 섀도우 지정, 드래그 앤 드롭 관련 데이터를 가지는 객체 지정, 0
                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);
                return false;

            }
        });


        veg2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                //롱클릭시 클립데이터를 만듬
                ClipData clip = ClipData.newPlainText("veg2", "veg2");
                //드래그할 데이터, 섀도우 지정, 드래그 앤 드롭 관련 데이터를 가지는 객체 지정, 0
                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);
                return false;

            }
        });

        veg3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                //롱클릭시 클립데이터를 만듬
                ClipData clip = ClipData.newPlainText("veg3", "veg3");
                //드래그할 데이터, 섀도우 지정, 드래그 앤 드롭 관련 데이터를 가지는 객체 지정, 0
                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);
                return false;

            }
        });

        sauce1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData clip = ClipData.newPlainText("sauce1", "sauce1");
                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);
                return false;

            }
        });

        sauce2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData clip = ClipData.newPlainText("sauce2", "sauce2");
                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);
                return false;

            }
        });

        sauce3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData clip = ClipData.newPlainText("sauce3", "sauce3");
                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);
                return false;

            }
        });

        sauce4.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData clip = ClipData.newPlainText("sauce4", "sauce4");
                v.startDrag(clip, new View.DragShadowBuilder(v), null, 0);
                return false;

            }
        });

        tableCook1.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                ImageButton btn;
                //드래그 객체가 버튼인지 확인
                if (v instanceof ImageButton) {
                    btn = (ImageButton) v;
                } else {
                    return false;
                }

                //이벤트를 받음
                switch (event.getAction()) {

                    //드래그가 시작되면
                    case DragEvent.ACTION_DRAG_STARTED:
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            return true;
                        } else {//인텐트의 경우 이쪽으로 와서 드래그를 받을 수가 없다.
                            return false;
                        }

                    //드래그가 드롭되면
                    case DragEvent.ACTION_DROP:
                        switch (event.getClipData().getItemAt(0).getText().toString()) {
                            case "bread1":
                                if (!dragFlag1) startTimer(0);
                                table1BreadImage.setImageResource(R.drawable.ic_bread_1);
                                ingredientCnt1--;
                                break;
                            case "veg1":
                                if (!dragFlag1) startTimer(0);
                                table1VegImage.setImageResource(R.drawable.ic_cabbage);
                                ingredientCnt1--;
                                break;
                            case "sauce1":
                                if (!dragFlag1) startTimer(0);
                                table1SauceImage.setImageResource(R.drawable.ic_sauce_1);
                                ingredientCnt1--;
                                break;
                            default:
                                unacceptableItem();
                        }

                        return true;
                    //드래그 성공 취소 여부에 상관없이 모든뷰에게
                    case DragEvent.ACTION_DRAG_ENDED:

                        if(event.getResult()){
                            if(ingredientCnt1 <=0) {
                                ingredientCnt1=3;
                                randItemCnt1--;
                                totalScore.setText(String.valueOf(score+=5));
                                orderItem1.setText(String.valueOf(randItemCnt1));
                                table1VegImage.setImageResource(R.drawable.ic_smart_cart);
                                table1SauceImage.setImageResource(R.drawable.ic_smart_cart);
                                table1BreadImage.setImageResource(R.drawable.ic_smart_cart);
                                tableTimer1.setText(String.valueOf(20) + "초");
                                stopTimer(0);
                            }
                        }
                        return true;
                }
                return true;
            }

        });

        tableCook2.setOnDragListener(new View.OnDragListener() {

            @Override
            public boolean onDrag(View v, DragEvent event) {
                ImageButton btn;
                //드래그 객체가 버튼인지 확인
                if (v instanceof ImageButton) {
                    btn = (ImageButton) v;

                } else {
                    return false;
                }

                switch (event.getAction()) {
                    //드래그가 시작되면
                    case DragEvent.ACTION_DRAG_STARTED:
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            return true;
                        } else {//인텐트의 경우 이쪽으로 와서 드래그를 받을 수가 없다.
                            return false;
                        }
                        //드래그가 드롭되면
                    case DragEvent.ACTION_DROP:
                        switch (event.getClipData().getItemAt(0).getText().toString()) {
                            case "bread2":
                                if (!dragFlag2) startTimer(1);
                                table2BreadImage.setImageResource(R.drawable.ic_bread_2);
                                ingredientCnt2--;
                                break;
                            case "veg2":
                                if (!dragFlag2) startTimer(1);
                                table2VegImage.setImageResource(R.drawable.ic_tomato);
                                ingredientCnt2--;
                                break;
                            case "sauce1":
                                if (!dragFlag2) startTimer(1);
                                table2Sauce1Image.setImageResource(R.drawable.ic_sauce_1);
                                ingredientCnt2--;
                                break;
                            case "sauce2":
                                if (!dragFlag2) startTimer(1);
                                table2Sauce2Image.setImageResource(R.drawable.ic_sauce_2);
                                ingredientCnt2--;
                                break;
                            default:
                                unacceptableItem();
                        }
                        return true;
                    //드래그 성공 취소 여부에 상관없이 모든뷰에게
                    case DragEvent.ACTION_DRAG_ENDED:
                        if(event.getResult()){//드래그 성공시
                            if(ingredientCnt2 <=0) {
                                randItemCnt2--;
                                ingredientCnt2=4;
                                totalScore.setText(String.valueOf(score+=10));
                                table2VegImage.setImageResource(R.drawable.ic_smart_cart);
                                table2Sauce1Image.setImageResource(R.drawable.ic_smart_cart);
                                table2Sauce2Image.setImageResource(R.drawable.ic_smart_cart);
                                table2BreadImage.setImageResource(R.drawable.ic_smart_cart);
                                tableTimer2.setText(String.valueOf(20) + "초");
                                orderItem2.setText(String.valueOf(randItemCnt2));
                                stopTimer(1);
                            }
                        }
                        return true;
                }
                return true;
            }

        });
        tableCook3.setOnDragListener(new View.OnDragListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onDrag(View v, DragEvent event) {
                ImageButton btn;
                //드래그 객체가 버튼인지 확인
                if (v instanceof ImageButton) {
                    btn = (ImageButton) v;

                } else {
                    return false;
                }

                switch (event.getAction()) {
                    //드래그가 시작되면
                    case DragEvent.ACTION_DRAG_STARTED:
                        if (event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                            return true;
                        } else {//인텐트의 경우 이쪽으로 와서 드래그를 받을 수가 없다.
                            return false;
                        }

                     //드래그가 드롭되면
                    case DragEvent.ACTION_DROP:
                        switch (event.getClipData().getItemAt(0).getText().toString()) {
                            case "bread2":
                                if (!dragFlag3) startTimer(2);
                                table3BreadImage.setImageResource(R.drawable.ic_bread_2);
                                ingredientCnt3--;
                                break;
                            case "veg1":
                                if (!dragFlag3) startTimer(2);
                                table3Veg1Image.setImageResource(R.drawable.ic_cabbage);
                                ingredientCnt3--;
                                break;
                            case "veg2":
                                if (!dragFlag3) startTimer(2);
                                table3Veg2Image.setImageResource(R.drawable.ic_tomato);
                                ingredientCnt3--;
                                break;
                            case "veg3":
                                if (!dragFlag3) startTimer(2);
                                table3Veg3Image.setImageResource(R.drawable.ic_onion);
                                ingredientCnt3--;
                                break;
                            case "sauce3":
                                if (!dragFlag3) startTimer(2);
                                table3Sauce1Image.setImageResource(R.drawable.ic_sauce_3);
                                ingredientCnt3--;
                                break;
                            case "sauce4":
                                if (!dragFlag3) startTimer(2);
                                table3Sauce2Image.setImageResource(R.drawable.ic_sauce_4);
                                ingredientCnt3--;
                                break;
                            default:
                                unacceptableItem();
                        }
                        return true;

                    //드래그 성공 취소 여부에 상관없이 모든뷰에게
                    case DragEvent.ACTION_DRAG_ENDED:

                        if(event.getResult()){//드래그 성공시
                            if(ingredientCnt3 <=0) {
                                ingredientCnt3=5;
                                randItemCnt3--;
                                totalScore.setText(String.valueOf(score+=20));
                                table3BreadImage.setImageResource(R.drawable.ic_smart_cart);
                                table3Veg1Image.setImageResource(R.drawable.ic_smart_cart);
                                table3Veg2Image.setImageResource(R.drawable.ic_smart_cart);
                                table3Veg3Image.setImageResource(R.drawable.ic_smart_cart);
                                table3Sauce1Image.setImageResource(R.drawable.ic_smart_cart);
                                table3Sauce2Image.setImageResource(R.drawable.ic_smart_cart);
                                tableTimer3.setText(String.valueOf(20) + "초");
                                orderItem3.setText(String.valueOf(randItemCnt3));
                                stopTimer(2);
                            }
                        }
                        return true;

                }
                return true;
            }

        });
    }

    private void startTimer(final int idx) {
        switch (idx) {
            case 0:
                dragFlag1 = true;
                timer1 = new Timer();
                timer1.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        tableTimerValue1--;
                        update(idx);
                        if (tableTimerValue1 == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    table1VegImage.setImageResource(R.drawable.ic_smart_cart);
                                    table1SauceImage.setImageResource(R.drawable.ic_smart_cart);
                                    table1BreadImage.setImageResource(R.drawable.ic_smart_cart);
                                    tableTimer1.setText(String.valueOf(20) + "초");
                                }
                            });
                            stopTimer(idx);
                        }
                    }
                }, 0, 1000);
                break;
            case 1:
                dragFlag2 = true;
                timer2 = new Timer();
                timer2.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        tableTimerValue2--;
                        update(idx);
                        if (tableTimerValue2 == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    table2VegImage.setImageResource(R.drawable.ic_smart_cart);
                                    table2Sauce1Image.setImageResource(R.drawable.ic_smart_cart);
                                    table2Sauce2Image.setImageResource(R.drawable.ic_smart_cart);
                                    table2BreadImage.setImageResource(R.drawable.ic_smart_cart);
                                    tableTimer2.setText(String.valueOf(20) + "초");
                                }
                            });
                            stopTimer(idx);
                        }
                    }
                }, 0, 1000);
                break;
            case 2:
                dragFlag3 = true;
                timer3 = new Timer();
                timer3.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        tableTimerValue3--;
                        update(idx);
                        if (tableTimerValue3 == 0) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    table3BreadImage.setImageResource(R.drawable.ic_smart_cart);
                                    table3Veg1Image.setImageResource(R.drawable.ic_smart_cart);
                                    table3Veg2Image.setImageResource(R.drawable.ic_smart_cart);
                                    table3Veg3Image.setImageResource(R.drawable.ic_smart_cart);
                                    table3Sauce1Image.setImageResource(R.drawable.ic_smart_cart);
                                    table3Sauce2Image.setImageResource(R.drawable.ic_smart_cart);
                                    tableTimer3.setText(String.valueOf(20) + "초");
                                }
                            });
                            stopTimer(idx);
                        }
                    }
                }, 0, 1000);
                break;
        }
    }

    private void stopTimer(final int idx) {
        switch (idx) {
            case 0:
                dragFlag1 = false;
                tableTimerValue1 = 20;
                timer1.cancel();
                break;
            case 1:
                dragFlag2 = false;
                tableTimerValue2 = 20;
                timer2.cancel();
                break;
            case 2:
                dragFlag3 = false;
                tableTimerValue3 = 20;
                timer3.cancel();
                break;
        }
    }

    private void update(final int idx) {
        Runnable updater = new Runnable() {
            public void run() {
                switch (idx) {
                    case 0:
                        tableTimer1.setText(String.valueOf(tableTimerValue1) + "초");
                        break;
                    case 1:
                        tableTimer2.setText(String.valueOf(tableTimerValue2) + "초");
                        break;
                    case 2:
                        tableTimer3.setText(String.valueOf(tableTimerValue3) + "초");
                        break;
                }

            }
        };
        switch (idx) {
            case 0:
                handler1.post(updater);
            case 1:
                handler2.post(updater);
            case 2:
                handler3.post(updater);
        }
    }

    public void unacceptableItem() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("잘못된 재료입니다.");
        dialog.show();
    }

    public void countDown(String time) {

        long conversionTime = 0;
        String getMin = time.substring(0, 2);
        String getSecond = time.substring(2, 4);


        if (getMin.substring(0, 1).equals("0")) {
            getMin = getMin.substring(1, 2);
        }

        if (getSecond.substring(0, 1).equals("0")) {
            getSecond = getSecond.substring(1, 2);
        }

        // 변환시간
        conversionTime = Long.parseLong(getMin) * 60 * 1000 + Long.parseLong(getSecond) * 1000;

        // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기))
        // 두번쨰 인자 : 주기( 1000 = 1초)
        new CountDownTimer(conversionTime, 1000) {

            // 특정 시간마다 뷰 변경
            @SuppressLint("SetTextI18n")
            public void onTick(long millisUntilFinished) {


                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000));
                String min = String.valueOf(getMin / (60 * 1000)); // 몫

                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지


                // 분이 한자리면 0을 붙인다
                if (min.length() == 1) {
                    min = "0" + min;
                }

                // 초가 한자리면 0을 붙인다
                if (second.length() == 1) {
                    second = "0" + second;
                }

                gameTimer.setText(min + ":" + second);
            }

            // 제한시간 종료시
            public void onFinish() {

                // 변경 후
                gameTimer.setText("게임종료");
                finishedDialog();

                // TODO : 타이머가 모두 종료될때 어떤 이벤트를 진행할지

            }
        }.start();

    }

    public void finishedDialog() {
        final EditText edittext = new EditText(this);

        finshedDialog = new AlertDialog.Builder(this);

        finshedDialog.setTitle("게임 종료");
        finshedDialog.setMessage("닉네임 입력");
        finshedDialog.setView(edittext);
        finshedDialog.setPositiveButton("저장",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),edittext.getText().toString() ,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LankActivity.class);
                        intent.putExtra("name", edittext.getText().toString());
                        intent.putExtra("score", totalScore.getText().toString());
                        startActivity(intent);
                    }
                });
        finshedDialog.setNegativeButton("종료",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
        finshedDialog.show();

    }

}
