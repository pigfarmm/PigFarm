package com.example.admin.pigfarm.Report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.pigfarm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Report_MomExclude extends AppCompatActivity {

    private String farm_id,gettextspn,sendtextspn,pdf;
    String getfarm_name,getunit_name,getfarm_id,m,d,m2,d2;
    TextView txt_farm, txt_unit;
    EditText edit_B1,edit_B2,edit_B5;
    Button btn_A1;
    Spinner spin_B3,spin_B4,spin_B5;
    ImageView img_B1,img_back,img_B2;
    Calendar StartDate = Calendar.getInstance();
    Calendar EndDate = Calendar.getInstance();

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
        spin_B5 = findViewById(R.id.spin_B5);
        btn_A1 = findViewById(R.id.btn_A1);
        img_back = findViewById(R.id.img_back);


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

        String date_lastday = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());
        edit_B2.setText(date_lastday);

        final String[] eventStr = getResources().getStringArray(R.array.condition_exclude);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, eventStr);
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spin_B3.setAdapter(adapterType);

        final String[] eventStr1 = getResources().getStringArray(R.array.condition_style);
        ArrayAdapter<String> adapterType1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, eventStr1);
        adapterType1.setDropDownViewResource(R.layout.spinner_item);
        spin_B4.setAdapter(adapterType1);

        spin_B3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        spin_B4.setEnabled(true);
                        spin_B5.setEnabled(true);

                        final String[] eventStr2 = getResources().getStringArray(R.array.condition_value_1);
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, eventStr2);
                        spin_B5.setAdapter(adapter);
                        break;


                    case 1:
                        spin_B4.setEnabled(true);
                        spin_B5.setEnabled(true);

                        final String[] eventStr3 = getResources().getStringArray(R.array.condition_value_3);
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, eventStr3);
                        spin_B5.setAdapter(adapter2);
                        break;

                    case 2:
                        spin_B4.setEnabled(true);
                        spin_B5.setEnabled(true);

                        final String[] eventStr4 = getResources().getStringArray(R.array.condition_value_2);
                        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, eventStr4);
                        spin_B5.setAdapter(adapter3);
                        break;

                    case 3:
                        spin_B4.setEnabled(true);
                        spin_B5.setEnabled(true);

                        final String[] eventStr5 = getResources().getStringArray(R.array.condition_value_4);
                        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, eventStr5);
                        spin_B5.setAdapter(adapter4);
                        break;

                    case 4:
                        spin_B4.setEnabled(false);
                        spin_B5.setEnabled(false);
                        break;

                }
            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        btn_A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (String.valueOf(spin_B3.getSelectedItem()).equals("จุดที่ถูกคัดทิ้ง") && String.valueOf(spin_B4.getSelectedItem()).equals("น้อยกว่า") && String.valueOf(spin_B5.getSelectedItem()).equals("เข้าฝูง-ผสม")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Report_MomExclude.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder.setCancelable(false);
                    builder.setMessage("ไม่มีรายงานนี้");
                    builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if (String.valueOf(spin_B3.getSelectedItem()).equals("ลักษณะการคัดทิ้ง") && String.valueOf(spin_B4.getSelectedItem()).equals("น้อยกว่า") && String.valueOf(spin_B5.getSelectedItem()).equals("ขาย")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Report_MomExclude.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder.setCancelable(false);
                    builder.setMessage("ไม่มีรายงานนี้");
                    builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if (String.valueOf(spin_B3.getSelectedItem()).equals("สาเหตุที่คัดทิ้ง") && String.valueOf(spin_B4.getSelectedItem()).equals("น้อยกว่า") && String.valueOf(spin_B5.getSelectedItem()).equals("ไม่เป็นสัด")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Report_MomExclude.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder.setCancelable(false);
                    builder.setMessage("ไม่มีรายงานนี้");
                    builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if (String.valueOf(spin_B3.getSelectedItem()).equals("ลำดับท้อง") && String.valueOf(spin_B4.getSelectedItem()).equals("น้อยกว่า") && String.valueOf(spin_B5.getSelectedItem()).equals("0")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Report_MomExclude.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder.setCancelable(false);
                    builder.setMessage("ไม่มีรายงานนี้");
                    builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else if (String.valueOf(spin_B3.getSelectedItem()).equals("สาเหตุที่คัดทิ้ง") && String.valueOf(spin_B4.getSelectedItem()).equals("มากกว่า") && String.valueOf(spin_B5.getSelectedItem()).equals("หนองไหล")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Report_MomExclude.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder.setCancelable(false);
                    builder.setMessage("ไม่มีรายงานนี้");
                    builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                else if(String.valueOf(spin_B3.getSelectedItem()).equals("จุดที่ถูกคัดทิ้ง")){
                    pdf = "https://pigaboo.xyz/Report/Mom_exclude_place.php";
                    Intent intent = new Intent(Report_MomExclude.this, WebView_MomExclude.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("start_date",edit_B1.getText().toString());
                    intent.putExtra("end_date",edit_B2.getText().toString());
                    intent.putExtra("condition_one",spin_B3.getSelectedItem().toString());
                    intent.putExtra("condition_two",spin_B4.getSelectedItem().toString());
                    intent.putExtra("condition_three",spin_B5.getSelectedItem().toString());

                    startActivity(intent);

                }else if(String.valueOf(spin_B3.getSelectedItem()).equals("ลักษณะการคัดทิ้ง")){
                    pdf = "https://pigaboo.xyz/Report/Mom_exclude_style.php";
                    Intent intent = new Intent(Report_MomExclude.this, WebView_MomExclude.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("start_date",edit_B1.getText().toString());
                    intent.putExtra("end_date",edit_B2.getText().toString());
                    intent.putExtra("condition_one",spin_B3.getSelectedItem().toString());
                    intent.putExtra("condition_two",spin_B4.getSelectedItem().toString());
                    intent.putExtra("condition_three",spin_B5.getSelectedItem().toString());

                    startActivity(intent);

                }else if(String.valueOf(spin_B3.getSelectedItem()).equals("สาเหตุที่คัดทิ้ง")){
                    pdf = "https://pigaboo.xyz/Report/Mom_exclude_cause.php";
                    Intent intent = new Intent(Report_MomExclude.this, WebView_MomExclude.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("start_date",edit_B1.getText().toString());
                    intent.putExtra("end_date",edit_B2.getText().toString());
                    intent.putExtra("condition_one",spin_B3.getSelectedItem().toString());
                    intent.putExtra("condition_two",spin_B4.getSelectedItem().toString());
                    intent.putExtra("condition_three",spin_B5.getSelectedItem().toString());

                    startActivity(intent);

                }else if(String.valueOf(spin_B3.getSelectedItem()).equals("ลำดับท้อง")){
                    pdf = "https://pigaboo.xyz/Report/Mom_exclude_preglist.php";
                    Intent intent = new Intent(Report_MomExclude.this, WebView_MomExclude.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("start_date",edit_B1.getText().toString());
                    intent.putExtra("end_date",edit_B2.getText().toString());
                    intent.putExtra("condition_one",spin_B3.getSelectedItem().toString());
                    intent.putExtra("condition_two",spin_B4.getSelectedItem().toString());
                    intent.putExtra("condition_three",spin_B5.getSelectedItem().toString());

                    startActivity(intent);

                }else if(String.valueOf(spin_B3.getSelectedItem()).equals("ไม่มีเงื่อนไข")){
                    pdf = "https://pigaboo.xyz/Report/Mom_exclude_nocon.php";
                    Intent intent = new Intent(Report_MomExclude.this, WebView_MomExclude.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("start_date",edit_B1.getText().toString());
                    intent.putExtra("end_date",edit_B2.getText().toString());
                    intent.putExtra("condition_one","");
                    intent.putExtra("condition_two","");
                    intent.putExtra("condition_three","");
                    startActivity(intent);

                }
//
            }
        });




    }

    public void showDatePicker_Start(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(Report_MomExclude.this, date, StartDate.get(Calendar.YEAR), StartDate.get(Calendar.MONTH), StartDate.get(Calendar.DAY_OF_MONTH));
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(Report_MomExclude.this, datee, EndDate.get(Calendar.YEAR), EndDate.get(Calendar.MONTH), EndDate.get(Calendar.DAY_OF_MONTH));
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
