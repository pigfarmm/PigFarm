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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.pigfarm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Report_compareunit extends AppCompatActivity {

    private String pdf;
    String getfarm_name,getunit_name,getfarm_id,m,d;
    TextView txt_farm, txt_unit,txt_showunit;
    Button btn_A1,btn_chooseunit;
    Spinner spnlengthtime;
    EditText edit_B1,edit_B2;
    ImageView img_B1,img_back;
    Calendar StartDate = Calendar.getInstance();
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<>();

    String[] array;
    boolean[] checkedItems;
    ArrayList<Integer> UnitItems = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_unit);

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
        spnlengthtime = findViewById(R.id.spnlengthtime);
        img_back = findViewById(R.id.img_back);
        btn_chooseunit = findViewById(R.id.btn_chooseunit);
        txt_showunit = findViewById(R.id.txt_showunit);

        final String[] eventStr = getResources().getStringArray(R.array.length_time_allperformance);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, eventStr);
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spnlengthtime.setAdapter(adapterType);

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
                Intent intent = new Intent(Report_compareunit.this, Report_Power.class);
                startActivity(intent);
                finish();
            }
        });


        String url = "https://pigaboo.xyz/select_unit.php?farm_id="+getfarm_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showUnit(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Wrong", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(stringRequest);

        btn_chooseunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Report_compareunit.this);
                builder.setTitle("เลือกยูนิตที่ต้องการเปรียบเทียบ");
                builder.setMultiChoiceItems(array, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position, boolean isChecked) {

                        if (isChecked) {
                            if (!UnitItems.contains(position)) {
                                UnitItems.add(position);
                            }
                        } else if (UnitItems.contains(position)) {
                            UnitItems.remove(position);
                        }
                    }

//                        if(isChecked){
//                            UnitItems.add(position);
//                        }else{
//                            UnitItems.remove(position);
//                        }
//                    }
                });

                builder.setCancelable(false);
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i=0; i < UnitItems.size(); i++){
                            item = item + array[UnitItems.get(i)];
                            if (i != UnitItems.size()-1){
                                item = item + ",";
                            }
                        }
                        txt_showunit.setText("ยูนิตที่เลือก : "+item);

                    }
                });
                builder.setNegativeButton("ยกเลิก", null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



        btn_A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UnitItems.size() != 0){
                    pdf = "https://pigaboo.xyz/Report/compare_unit.php";
                    Intent intent = new Intent(Report_compareunit.this, WebView_CompareUnit.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("last_day",edit_B1.getText().toString());
                    intent.putExtra("ip_number",edit_B2.getText().toString());
                    intent.putExtra("ip_type",spnlengthtime.getSelectedItem().toString());
                    intent.putExtra("list",txt_showunit.getText().toString());
//                    intent.putParcelableArrayListExtra("list",seletedItems);

                    startActivity(intent);
                }else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Report_compareunit.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder1.setCancelable(false);
                    builder1.setMessage("กรุณาเลือกยูนิตที่ต้องการเปรียบเทียบก่อน");
                    builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder1.create();
                    dialog.show();
                }

            }
        });



    }

    private void showUnit(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");


            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);
                list.add(collectData.getString("unit_name"));
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }

        listItems.addAll(list);

        array = listItems.toArray(new String[listItems.size()]);
        checkedItems = new boolean[array.length];




    }

    public void showDatePicker(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(Report_compareunit.this, date, StartDate.get(Calendar.YEAR), StartDate.get(Calendar.MONTH), StartDate.get(Calendar.DAY_OF_MONTH));
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
