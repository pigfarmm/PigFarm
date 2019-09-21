package com.example.admin.pigfarm.Report;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.example.admin.pigfarm.Home;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Report_History extends AppCompatActivity {

    String farm_id,url,url2,pdf,pig_id;
    Object selectedItem,selectedItem2,selectedItem3;
    TextView txt_A1_8;
    Spinner spin_A1_1,spin_A1_7,spin_A1_8;
    ArrayList<String> listItemsMom = new ArrayList<>();
    ArrayList<String> listItemsDad = new ArrayList<>();
    ArrayList<String> listMom = new ArrayList<>();
    ArrayList<String> listDad = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;
    Button btn_A1;
    String getfarm_name,getunit_name,getfarm_id;
    TextView txt_farm, txt_unit;
    ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_history);

        img_back = findViewById(R.id.img_back);
        txt_farm = findViewById(R.id.txt_farm);
        txt_unit = findViewById(R.id.txt_unit);

        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_name = farm.getString("farm_name", "");
        getunit_name = farm.getString("unit_name", "");
        getfarm_id = farm.getString("farm_id","");
        txt_farm.setText(getfarm_name);
        txt_unit.setText(getunit_name);

        spin_A1_1 = findViewById(R.id.spin_A1_1);
        spin_A1_7 = findViewById(R.id.spin_A1_7);
        spin_A1_8 = findViewById(R.id.spin_A1_8);
        txt_A1_8 = findViewById(R.id.txt_A1_8);
        btn_A1 = findViewById(R.id.btn_A1);

        url = "https://pigaboo.xyz/Query_pigid.php?farm_id="+getfarm_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                QueryMomID(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Report_History.this, "ไม่สามารถดึงข้อมูลได้", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(stringRequest);

        url2 = "https://pigaboo.xyz/Query_DadID.php?farm_id="+getfarm_id;
        StringRequest stringRequest1 = new StringRequest(url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                QueryDadID(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Report_History.this, "ไม่สามารถดึงข้อมูลได้", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue1 = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue1.add(stringRequest1);



        final String[] eventStr = getResources().getStringArray(R.array.history);
        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, eventStr);
        adapterType.setDropDownViewResource(R.layout.spinner_item);
        spin_A1_1.setAdapter(adapterType);

        final String[] eventStr2 = getResources().getStringArray(R.array.type_of_history);
        final ArrayAdapter<String> adapterSort = new ArrayAdapter<String>(getApplicationContext(),
                android.R.layout.simple_spinner_dropdown_item, eventStr2);
        adapterSort.setDropDownViewResource(R.layout.spinner_item);
        spin_A1_7.setAdapter(adapterSort);


        spin_A1_1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                selectedItem = adapterView.getItemAtPosition(position);
                Log.d("pig","value: "+position);
                switch (position){
                    case 0:
                        listItemsDad.clear();
                        txt_A1_8.setText("เบอร์แม่");
                        listItemsMom.addAll(listMom);
                        adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listItemsMom);
                        spin_A1_8.setAdapter(adapter);

                        spin_A1_7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adtv, View view, int pos, long l) {
                                selectedItem2 = adtv.getItemAtPosition(pos);
                                if (pos == 0){
                                listItemsDad.clear();
                                listItemsMom.clear();
                                txt_A1_8.setText("เบอร์แม่");
                                listItemsMom.addAll(listMom);
                                adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listItemsMom);
                                spin_A1_8.setAdapter(adapter);
                            }else{
                                txt_A1_8.setText("สถานภาพ");
                                final String[] eventStr3 = getResources().getStringArray(R.array.type_of_status2);
                                final ArrayAdapter<String> adapterTypeStatus = new ArrayAdapter<String>(getApplicationContext(),
                                        android.R.layout.simple_spinner_dropdown_item, eventStr3);
                                adapterTypeStatus.setDropDownViewResource(R.layout.spinner_item);
                                spin_A1_8.setAdapter(adapterTypeStatus);
                            }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;

                    case 1:
                        listItemsDad.clear();
                        txt_A1_8.setText("เบอร์พ่อ");
                        listItemsDad.addAll(listDad);
                        adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listItemsDad);
                        spin_A1_8.setAdapter(adapter1);

                        spin_A1_7.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adtv2, View view, int i, long l) {
                                selectedItem3 = adtv2.getItemAtPosition(i);
                                if (i == 0){
                                    listItemsMom.clear();
                                    listItemsDad.clear();
                                    txt_A1_8.setText("เบอร์พ่อ");
                                    listItemsDad.addAll(listDad);
                                    adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listItemsDad);
                                    spin_A1_8.setAdapter(adapter1);
                                }else{
                                    txt_A1_8.setText("สถานภาพ");
                                    final String[] eventStr3 = getResources().getStringArray(R.array.type_of_status2);
                                    final ArrayAdapter<String> adapterTypeStatus = new ArrayAdapter<String>(getApplicationContext(),
                                            android.R.layout.simple_spinner_dropdown_item, eventStr3);
                                    adapterTypeStatus.setDropDownViewResource(R.layout.spinner_item);
                                    spin_A1_8.setAdapter(adapterTypeStatus);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        break;

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        btn_A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(spin_A1_1.getSelectedItem()).equals("แม่พันธุ์") && String.valueOf(spin_A1_7.getSelectedItem()).equals("เบอร์หู")){

                    pdf = "https://pigaboo.xyz/Report/History_by_ID.php";
                    pig_id = spin_A1_8.getSelectedItem().toString();
                    Intent intent = new Intent(Report_History.this, WebView_Report_History.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("pig_id",pig_id);
                    startActivity(intent);
                }

                if (String.valueOf(spin_A1_1.getSelectedItem()).equals("แม่พันธุ์") && String.valueOf(spin_A1_7.getSelectedItem()).equals("สถานภาพ") && String.valueOf(spin_A1_8.getSelectedItem()).equals("หมูในฝูง")){

                    pdf = "https://pigaboo.xyz/Report/History_by_PigAlive.php";
                    Intent intent = new Intent(Report_History.this, WebView_Report_History.class);
                    intent.putExtra("url",pdf);
                    startActivity(intent);
                }

                if (String.valueOf(spin_A1_1.getSelectedItem()).equals("แม่พันธุ์") && String.valueOf(spin_A1_7.getSelectedItem()).equals("สถานภาพ") && String.valueOf(spin_A1_8.getSelectedItem()).equals("หมูคัดทิ้ง")){

                    pdf = "https://pigaboo.xyz/Report/History_by_Exclude.php";
                    Intent intent = new Intent(Report_History.this, WebView_Report_History.class);
                    intent.putExtra("url",pdf);
                    startActivity(intent);
                }

                if (String.valueOf(spin_A1_1.getSelectedItem()).equals("แม่พันธุ์") && String.valueOf(spin_A1_7.getSelectedItem()).equals("สถานภาพ") && String.valueOf(spin_A1_8.getSelectedItem()).equals("ทั้งหมด")){

                    pdf = "https://pigaboo.xyz/Report/History_by_All.php";
                    Intent intent = new Intent(Report_History.this, WebView_Report_History.class);
                    intent.putExtra("url",pdf);
                    startActivity(intent);
                }

                if (String.valueOf(spin_A1_1.getSelectedItem()).equals("พ่อพันธุ์") && String.valueOf(spin_A1_7.getSelectedItem()).equals("เบอร์หู")){

                    pdf = "https://pigaboo.xyz/Report/HistoryDAD_by_ID.php";
                    pig_id = spin_A1_8.getSelectedItem().toString();
                    Intent intent = new Intent(Report_History.this, WebView_Report_History.class);
                    intent.putExtra("url",pdf);
                    intent.putExtra("pig_id",pig_id);
                    startActivity(intent);
                }

                if (String.valueOf(spin_A1_1.getSelectedItem()).equals("พ่อพันธุ์") && String.valueOf(spin_A1_7.getSelectedItem()).equals("สถานภาพ") && String.valueOf(spin_A1_8.getSelectedItem()).equals("หมูในฝูง")){

                    pdf = "https://pigaboo.xyz/Report/HistoryDAD_by_All.php";
                    Intent intent = new Intent(Report_History.this, WebView_Report_History.class);
                    intent.putExtra("url",pdf);
                    startActivity(intent);
                }

                if (String.valueOf(spin_A1_1.getSelectedItem()).equals("พ่อพันธุ์") && String.valueOf(spin_A1_7.getSelectedItem()).equals("สถานภาพ") && String.valueOf(spin_A1_8.getSelectedItem()).equals("ทั้งหมด")){

                    pdf = "https://pigaboo.xyz/Report/HistoryDAD_by_All.php";
                    Intent intent = new Intent(Report_History.this, WebView_Report_History.class);
                    intent.putExtra("url",pdf);
                    startActivity(intent);
                }


            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Report_History.this, Home.class);
                startActivity(intent);
                finish();
            }
        });



    }


    public void QueryMomID(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            if (result.length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Report_History.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                builder.setCancelable(false);
                builder.setMessage("ท่านยังไม่ได้เปิดประวัติสุกร");
                builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);
                listMom.add(collectData.getString("pig_id"));
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }


    }


    public void QueryDadID(String response){
        try {
            JSONObject jsonObject2 = new JSONObject(response);
            JSONArray result2 = jsonObject2.getJSONArray("result");

            if (result2.length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Report_History.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
                builder.setCancelable(false);
                builder.setMessage("ท่านยังไม่ได้เปิดประวัติพ่อพันธุ์");
                builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            for (int i = 0; i<result2.length(); i++){
                JSONObject collectData2 = result2.getJSONObject(i);
                listDad.add(collectData2.getString("pig_id"));
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
