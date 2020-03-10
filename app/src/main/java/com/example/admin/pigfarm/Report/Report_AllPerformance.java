package com.example.admin.pigfarm.Report;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.pigfarm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Report_AllPerformance extends AppCompatActivity {

    private String farm_id,gettextspn,sendtextspn,pdf;
    String getfarm_name,getunit_name,getfarm_id,m,d;
    TextView txt_farm, txt_unit;
    Spinner spnlengthtime;
    EditText edit_B1, edit_B3, edit_B2;
    Button btn_A1;
    ImageView img_B1,img_back;
    Calendar StartDate = Calendar.getInstance();
    Calendar EndDate = Calendar.getInstance();



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_b1);

//        img_back = findViewById(R.id.img_back);
        txt_farm = findViewById(R.id.txt_farm);
        txt_unit = findViewById(R.id.txt_unit);

        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_name = farm.getString("farm_name", "");
        getunit_name = farm.getString("unit_name", "");
        getfarm_id = farm.getString("farm_id","");
        txt_farm.setText(getfarm_name);
        txt_unit.setText(getunit_name);

        spnlengthtime = findViewById(R.id.spnlengthtime);
        edit_B1 = findViewById(R.id.edit_B1);
        edit_B2 = findViewById(R.id.edit_B2);
        edit_B3 = findViewById(R.id.edit_B3);
        btn_A1 = findViewById(R.id.btn_A1);
        img_B1 = findViewById(R.id.img_B1);
        img_back = findViewById(R.id.img_back);

        String date_lastday = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());
        edit_B1.setText(date_lastday);

        img_B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_AllPerformance.this, Report_Power.class);
                startActivity(intent);
                finish();
            }
        });

        edit_B3.setFilters(new InputFilter[]{new InputFilterMinMax("1","12")});




        final String[] eventStr = getResources().getStringArray(R.array.length_time_allperformance);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, eventStr);
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spnlengthtime.setAdapter(adapterType);


        btn_A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pdf = "https://pigaboo.xyz/Report/Performance_overall.php";
                Intent intent = new Intent(Report_AllPerformance.this, WebView_AllPerformance.class);
                intent.putExtra("url",pdf);
                intent.putExtra("last_day",edit_B1.getText().toString());
                intent.putExtra("range",edit_B3.getText().toString());
                intent.putExtra("ip_number",edit_B2.getText().toString());
                intent.putExtra("ip_type",spnlengthtime.getSelectedItem().toString());
                startActivity(intent);
            }
        });



    }

    public void showDatePicker(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(Report_AllPerformance.this, date, StartDate.get(Calendar.YEAR), StartDate.get(Calendar.MONTH), StartDate.get(Calendar.DAY_OF_MONTH));
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
}
