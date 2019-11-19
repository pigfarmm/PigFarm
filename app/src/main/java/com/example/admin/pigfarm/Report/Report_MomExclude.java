package com.example.admin.pigfarm.Report;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.pigfarm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Report_MomExclude extends AppCompatActivity {

    private String farm_id,gettextspn,sendtextspn,pdf;
    String getfarm_name,getunit_name,getfarm_id,m,d;
    TextView txt_farm, txt_unit;
    EditText edit_B1,edit_B2,edit_B5;
    Button btn_A1;
    Spinner spin_B3,spin_B4;
    ImageView img_B1,img_back,img_B2;
    Calendar StartDate = Calendar.getInstance();
    Calendar StartDate2 = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_b7);

        txt_farm = findViewById(R.id.txt_farm);
        txt_unit = findViewById(R.id.txt_unit);

        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_name = farm.getString("farm_name", "");
        getunit_name = farm.getString("unit_name", "");
        getfarm_id = farm.getString("farm_id","");
        txt_farm.setText(getfarm_name);
        txt_unit.setText(getunit_name);

        edit_B1 = findViewById(R.id.edit_B1);
        edit_B2 = findViewById(R.id.edit_B2);
        edit_B5 = findViewById(R.id.edit_B5);
        img_B1 = findViewById(R.id.img_B1);
        img_B2 = findViewById(R.id.img_B2);
        spin_B3 = findViewById(R.id.spin_B3);
        spin_B4 = findViewById(R.id.spin_B4);
        btn_A1 = findViewById(R.id.btn_A1);

        String date_lastday = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());
        edit_B1.setText(date_lastday);

        img_B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDatePicker();
            }
        });

        img_B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                showDatePicker_2();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_MomExclude.this, Report_Power.class);
                startActivity(intent);
                finish();
            }
        });

        final String[] eventStr = getResources().getStringArray(R.array.length_time_allperformance);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, eventStr);
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spin_B3.setAdapter(adapterType);


    }
}
