package com.example.admin.pigfarm.BodyAnalyze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.pigfarm.BCS_Fragment;
import com.example.admin.pigfarm.Event_main;
import com.example.admin.pigfarm.Home;
import com.example.admin.pigfarm.R;
import com.example.admin.pigfarm.Report.Report_BredderDad;
import com.example.admin.pigfarm.Report.Report_Power;

import java.math.BigDecimal;

public class HomeBCS extends AppCompatActivity {

    TextView txt_farm, txt_unit,txt_ฺtake1_2,txt_ฺtake2_2,txt_ฺtake1_2_1,txt_ฺtake2_2_1;
    ImageView img_back;
    Button btn_take1,btn_take2,btn_A1,btn_reset;
    String getfarm_name,getunit_name,getfarm_id,getscore,getscore2,fragment_id;
    double sum2 = 0.0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_bcs);


        txt_farm = findViewById(R.id.txt_farm);
        txt_unit = findViewById(R.id.txt_unit);
        txt_ฺtake1_2 = findViewById(R.id.txt_ฺtake1_2);
        txt_ฺtake2_2 = findViewById(R.id.txt_ฺtake2_2);
        btn_take1 = findViewById(R.id.btn_take1);
        btn_take2 = findViewById(R.id.btn_take2);
        img_back = findViewById(R.id.img_back);
        txt_ฺtake1_2_1 = findViewById(R.id.txt_ฺtake1_2_1);
        txt_ฺtake2_2_1 = findViewById(R.id.txt_ฺtake2_2_1);
        btn_A1 = findViewById(R.id.btn_A1);
        btn_reset = findViewById(R.id.btn_reset);


        SharedPreferences fm = getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
        fragment_id = fm.getString("fragment_id", "");

        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_name = farm.getString("farm_name", "");
        getunit_name = farm.getString("unit_name", "");
        getfarm_id = farm.getString("farm_id","");
        txt_farm.setText(getfarm_name);
        txt_unit.setText(getunit_name);

        SharedPreferences score1 = getSharedPreferences("score", Context.MODE_PRIVATE);
        getscore = score1.getString("score1", "");
        if (getscore != null){
            txt_ฺtake1_2_1.setText(getscore);
        }else{
            txt_ฺtake1_2_1.setText("0");
        }

        SharedPreferences score2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
        getscore2 = score2.getString("score2", "");
        if (getscore2 != null){
            txt_ฺtake2_2_1.setText(getscore2);
        }else{
            txt_ฺtake2_2_1.setText("0");
        }


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeBCS.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        btn_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.remove("score1");
                editor.commit();

                SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = shared2.edit();
                editor2.remove("score2");
                editor2.commit();

                txt_ฺtake1_2_1.setText("0");
                txt_ฺtake2_2_1.setText("0");
            }
        });


        btn_take1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeBCS.this, CameraActivity.class);
                startActivity(intent);
                finish();



            }
        });

        btn_take2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeBCS.this, CameraActivity2.class);
                startActivity(intent);
                finish();


            }
        });

        btn_A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String replace = getscore.replaceAll("(\r\n|\n)", "");
                    String replace2 = getscore2.replaceAll("(\r\n|\n)", "");

                    String replace_white = replace.replaceAll("    ","");
                    String replace_white2 = replace2.replaceAll("    ","");

                    Log.d("check",replace_white);

                    if(replace_white == ""){
                        replace_white = "0.0";
                    }else if(replace_white2 == ""){
                        replace_white2 = "0.0";
                    }


                final double score1 = Double.parseDouble(replace_white);
                final double score2 = Double.parseDouble(replace_white2);

                final double sum = (score1+score2)/2;


               if(sum == 2){
                   sum2 = 2;
               }else if(sum == 2.25){
                   sum2 = 2;
               }else if(sum == 2.5){
                   sum2 = 2.5;
               }else if(sum == 2.75){
                   sum2 = 3;
               }else if(sum == 3){
                   sum2 = 3;
               }else if(sum == 3.25){
                   sum2 = 3;
               }else if(sum == 3.5){
                   sum2 = 3.5;
               }else if(sum == 3.75){
                   sum2 = 4;
               }else if(sum == 4){
                   sum2 = 4;
               }else if(sum == 1.25){
                   sum2 = 2.5;
               }else if(sum == 1.5){
                   sum2 = 3;
               }else if(sum == 1.0){
                   sum2 = 2;
               }else if(sum == 1.75){
                   sum2 = 3.5;
               }else if(sum == 2){
                   sum2 = 4;
               }else if(sum == 0){
                   sum2 = 0;
               }

                Log.d("sum2",String.valueOf(sum2));




                if (fragment_id.equals("1")) {

                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "1");
                    startActivity(intent);
                    finish();

                }else if (fragment_id.equals("2")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "2");
                    startActivity(intent);
                    finish();

                }else if (fragment_id.equals("3")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "3");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("4")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "4");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("5")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "5");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("6")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "6");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("7")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "7");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("8")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "8");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("9")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "9");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("10")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "10");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("11")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "11");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("12")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "12");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("13")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "13");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("14")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "14");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("15")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "15");
                    startActivity(intent);
                    finish();
                }else if (fragment_id.equals("16")){
                    SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.remove("score1");
                    editor.commit();

                    SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = shared2.edit();
                    editor2.remove("score2");
                    editor2.commit();

                    SharedPreferences shared3 = getApplicationContext().getSharedPreferences("sum_score", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3 = shared3.edit();
                    editor3.putString("sum_score", String.valueOf(sum2));
                    editor3.commit();

                    SharedPreferences shared4 = getApplicationContext().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4 = shared4.edit();
                    editor4.putString("fragment_id", String.valueOf(sum2));
                    editor4.commit();


                    Intent intent = new Intent(HomeBCS.this, Event_main.class);
                    intent.putExtra("fragment_id", "16");
                    startActivity(intent);
                    finish();
                }


            }
        });







    }
}
