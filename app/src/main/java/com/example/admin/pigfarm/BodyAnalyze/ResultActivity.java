package com.example.admin.pigfarm.BodyAnalyze;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.admin.pigfarm.R;

public class ResultActivity extends AppCompatActivity {

    String getscore;
    TextView txt_Score;
    Button next_button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_process);

        getscore = getIntent().getExtras().getString("score");

        next_button = findViewById(R.id.next_button);
        txt_Score = findViewById(R.id.txt_Score);
        txt_Score.setText(getscore);


        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this,InsertBodyActivity.class);
                intent.putExtra("score",getscore);
                startActivity(intent);
            }
        });

    }
}
