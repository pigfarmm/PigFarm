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
import com.example.admin.pigfarm.Event_main;
import com.example.admin.pigfarm.Home;

public class Report_Home extends AppCompatActivity {

    private String farm_id;
    String getfarm_name,getunit_name,getfarm_id;
    TextView txt_farm, txt_unit;
    ImageView img_back;
    CardView card_groupmanage, card_status_of_dadmom,card_history_of_dadmom;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_home);

        img_back = findViewById(R.id.img_back);
        txt_farm = findViewById(R.id.txt_farm);
        txt_unit = findViewById(R.id.txt_unit);

        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_name = farm.getString("farm_name", "");
        getunit_name = farm.getString("unit_name", "");
        getfarm_id = farm.getString("farm_id","");
        txt_farm.setText(getfarm_name);
        txt_unit.setText(getunit_name);

        card_groupmanage = findViewById(R.id.card_groupmanage);
//        card_status_of_dadmom = findViewById(R.id.card_status_of_dadmom);
        card_history_of_dadmom = findViewById(R.id.card_history_of_dadmom);

        card_groupmanage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Report_Home.this, Report_Group.class);
                startActivity(intent);
            }
        });

//        card_status_of_dadmom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(Report_Home.this, Report_Status.class);
//                startActivity(intent);
//            }
//        });

        card_history_of_dadmom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Report_Home.this, Report_History.class);  //Report_History
                startActivity(intent);
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_Home.this, Report_Main.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
