package com.example.admin.pigfarm.BodyAnalyze;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.pigfarm.R;

public class ResultActivity extends AppCompatActivity {

    String getscore;
    TextView txt_Score,txt_percent2;
    Button next_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_process);

        getscore = getIntent().getExtras().getString("score1");


        String substr = getscore.substring(0,3);
//        String substr2 = getscore.substring(5);

        next_button = findViewById(R.id.next_button);
        txt_Score = findViewById(R.id.txt_Score);
//        txt_percent2 = findViewById(R.id.txt_percent2);

        txt_Score.setText(substr);
//        txt_percent2.setText(substr2);

        SharedPreferences shared = getApplicationContext().getSharedPreferences("score", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("score1",getscore);
        editor.commit();


        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,HomeBCS.class);
//                intent.putExtra("score",getscore);
                startActivity(intent);
                finish();

            }
        });

    }
}
