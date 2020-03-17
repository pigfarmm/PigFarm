package com.example.admin.pigfarm.Report;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.pigfarm.Home;
import com.example.admin.pigfarm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Report_BCS extends AppCompatActivity {

    private String farm_id,pdf;
    String getfarm_name,getunit_name,getfarm_id,m,d,m2,d2;
    TextView txt_farm, txt_unit;
    EditText edit_B1,edit_B2;
    Button btn_A1;
    Spinner spin_B3;
    ImageView img_B1,img_B2,img_back;
    Calendar StartDate = Calendar.getInstance();
    Calendar EndDate = Calendar.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_bcs);

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
        btn_A1 = findViewById(R.id.btn_A1);
        img_B1 = findViewById(R.id.img_B1);
        img_B2 = findViewById(R.id.img_B2);
        spin_B3 = findViewById(R.id.spin_B3);
        img_back = findViewById(R.id.img_back);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_BCS.this, Home.class);
                startActivity(intent);
                finish();
            }
        });


        String date_lastday = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());
        edit_B2.setText(date_lastday);

        img_B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker_Start();
            }
        });

        img_B2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker_End();
            }
        });

        final String[] eventStr = getResources().getStringArray(R.array.Report_BCS);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, eventStr);
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spin_B3.setAdapter(adapterType);

        btn_A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(String.valueOf(spin_B3.getSelectedItem()).equals("ผสมพันธุ์")){
                    pdf = "https://pigaboo.xyz/Report/ReportBCS_Breed.php";
                    Intent intent = new Intent(Report_BCS.this, WebView_BCS.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("start_date",edit_B1.getText().toString());
                    intent.putExtra("end_date",edit_B2.getText().toString());

                    startActivity(intent);

                }else if(String.valueOf(spin_B3.getSelectedItem()).equals("คลอด")){
                    pdf = "https://pigaboo.xyz/Report/ReportBCS_GiveBirth.php";
                    Intent intent = new Intent(Report_BCS.this, WebView_BCS.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("start_date",edit_B1.getText().toString());
                    intent.putExtra("end_date",edit_B2.getText().toString());

                    startActivity(intent);

                }else if(String.valueOf(spin_B3.getSelectedItem()).equals("หย่านม")){
                    pdf = "https://pigaboo.xyz/Report/ReportBCS_Wean.php";
                    Intent intent = new Intent(Report_BCS.this, WebView_BCS.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("start_date",edit_B1.getText().toString());
                    intent.putExtra("end_date",edit_B2.getText().toString());

                    startActivity(intent);

                }
            }
        });


    }

    public void showDatePicker_Start(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(Report_BCS.this, date, StartDate.get(Calendar.YEAR), StartDate.get(Calendar.MONTH), StartDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            StartDate.set(Calendar.YEAR, year);
            StartDate.set(Calendar.MONTH, monthOfYear);
            StartDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            monthOfYear = monthOfYear + 1;
            if (monthOfYear < 10){
                m = "0"+monthOfYear;
            }else{
                m = String.valueOf(monthOfYear);
            }
            if (dayOfMonth < 10){
                d = "0"+dayOfMonth;
            }else{
                d = String.valueOf(dayOfMonth);
            }
            edit_B1.setText(year+"-"+m+"-"+d);
        }
    };

    public void showDatePicker_End(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(Report_BCS.this, datee, EndDate.get(Calendar.YEAR), EndDate.get(Calendar.MONTH), EndDate.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener datee = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            EndDate.set(Calendar.YEAR, year);
            EndDate.set(Calendar.MONTH, monthOfYear);
            EndDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            monthOfYear = monthOfYear + 1;
            if (monthOfYear < 10){
                m2 = "0"+monthOfYear;
            }else{
                m2 = String.valueOf(monthOfYear);
            }
            if (dayOfMonth < 10){
                d2 = "0"+dayOfMonth;
            }else{
                d2 = String.valueOf(dayOfMonth);
            }
            edit_B2.setText(year+"-"+m2+"-"+d2);
        }
    };
}
