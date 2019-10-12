package com.example.admin.pigfarm.Report;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.pigfarm.R;

public class Report_Power extends AppCompatActivity {

    private String farm_id;
    String getfarm_name,getunit_name,getfarm_id;
    TextView txt_farm, txt_unit;
    ImageView img_back;
    CardView card_allperformance, card_b2,card_b3,card_b5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_b);

        img_back = findViewById(R.id.img_back);
        txt_farm = findViewById(R.id.txt_farm);
        txt_unit = findViewById(R.id.txt_unit);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_Power.this, Report_Main.class);
                startActivity(intent);
                finish();
            }
        });

        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_name = farm.getString("farm_name", "");
        getunit_name = farm.getString("unit_name", "");
        getfarm_id = farm.getString("farm_id","");
        txt_farm.setText(getfarm_name);
        txt_unit.setText(getunit_name);

        card_allperformance = findViewById(R.id.card_allperformance);
        card_b2 = findViewById(R.id.card_b2);
        card_b3 = findViewById(R.id.card_b3);
        card_b5 = findViewById(R.id.card_b5);

        card_allperformance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_Power.this, Report_AllPerformance.class);
                startActivity(intent);
            }
        });

        card_b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_Power.this, Report_BredderDad.class);
                startActivity(intent);
            }
        });

        card_b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_Power.this, Report_GroupBredder.class);
                startActivity(intent);
            }
        });

        card_b5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_Power.this, Report_Exclude.class);
                startActivity(intent);
            }
        });






    }
}
