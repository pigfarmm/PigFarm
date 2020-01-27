package com.example.admin.pigfarm.Report;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

public class Report_piggydead extends AppCompatActivity {

    private String farm_id,gettextspn,sendtextspn,pdf;
    String getfarm_name,getunit_name,getfarm_id,m,d;
    TextView txt_farm, txt_unit;
    EditText edit_B1,edit_B2,pregnant_edt3_1,pregnant_edt3_2,pregnant_edt4_1,pregnant_edt4_2,edit_B5;
    Button btn_A1;
    Spinner spnlengthtime,select_type_age_piggy_2,select_type_age_piggy;
    ImageView img_B1,img_back;
    Calendar StartDate = Calendar.getInstance();
    Calendar EndDate = Calendar.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_b6);

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
        pregnant_edt3_1 = findViewById(R.id.pregnant_edt3_1);
        pregnant_edt3_2 = findViewById(R.id.pregnant_edt3_2);
        pregnant_edt4_1 = findViewById(R.id.pregnant_edt4_1);
        pregnant_edt4_2 = findViewById(R.id.pregnant_edt4_2);
        edit_B5 = findViewById(R.id.edit_B5);
        btn_A1 = findViewById(R.id.btn_A1);
        img_B1 = findViewById(R.id.img_B1);
        spnlengthtime = findViewById(R.id.spnlengthtime);
        select_type_age_piggy = findViewById(R.id.select_type_age_piggy);
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
                Intent intent = new Intent(Report_piggydead.this, Report_Power.class);
                startActivity(intent);
                finish();
            }
        });

        edit_B5.setFilters(new InputFilter[]{new InputFilterMinMax("1","12")});

        final String[] eventStr = getResources().getStringArray(R.array.length_time_allperformance);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, eventStr);
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spnlengthtime.setAdapter(adapterType);

        final String[] eventStr2 = getResources().getStringArray(R.array.select_type_b3);
        ArrayAdapter<String> adapterType2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, eventStr2);
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        select_type_age_piggy.setAdapter(adapterType2);


        btn_A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_type_age_piggy.getSelectedItem().toString().equals("จำแนกเป็นช่วง")){
                    pdf = "https://pigaboo.xyz/Report/Performance_piggydead.php";
                    Intent intent = new Intent(Report_piggydead.this, WebView_Piggydead.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("last_day",edit_B1.getText().toString());
                    intent.putExtra("ip_number",edit_B2.getText().toString());
                    intent.putExtra("ip_type",spnlengthtime.getSelectedItem().toString());
                    intent.putExtra("start_age",pregnant_edt3_1.getText().toString());
                    intent.putExtra("end_age",pregnant_edt3_2.getText().toString());
                    intent.putExtra("start_pregnant",pregnant_edt4_1.getText().toString());
                    intent.putExtra("end_pregnant",pregnant_edt4_2.getText().toString());
                    intent.putExtra("amount_lengthtime",edit_B1.getText().toString());

                    startActivity(intent);
                }else{
                    pdf = "https://pigaboo.xyz/Report/Performance_piggydead_all.php";
                    Intent intent = new Intent(Report_piggydead.this, WebView_Piggydead.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("last_day",edit_B1.getText().toString());
                    intent.putExtra("ip_number",edit_B2.getText().toString());
                    intent.putExtra("ip_type",spnlengthtime.getSelectedItem().toString());
                    intent.putExtra("start_age",pregnant_edt3_1.getText().toString());
                    intent.putExtra("end_age",pregnant_edt3_2.getText().toString());
                    intent.putExtra("start_pregnant",pregnant_edt4_1.getText().toString());
                    intent.putExtra("end_pregnant",pregnant_edt4_2.getText().toString());
                    intent.putExtra("amount_lengthtime",edit_B1.getText().toString());

                    startActivity(intent);
                }

            }
        });

    }

    public void showDatePicker(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(Report_piggydead.this, date, StartDate.get(Calendar.YEAR), StartDate.get(Calendar.MONTH), StartDate.get(Calendar.DAY_OF_MONTH));
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
