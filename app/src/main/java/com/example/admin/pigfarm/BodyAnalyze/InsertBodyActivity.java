package com.example.admin.pigfarm.BodyAnalyze;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.pigfarm.Breed_Fragment;
import com.example.admin.pigfarm.Home;
import com.example.admin.pigfarm.LoginActivity;
import com.example.admin.pigfarm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class InsertBodyActivity extends AppCompatActivity{

    EditText edit_A1,result_bcs;
    Button btn_A1;
    TextView txt_farm,txt_unit;
    String unit_name,farm_name,farm_id,unit_id;
    Spinner spin_A1_1;
    ArrayAdapter<String> adapter;
    private String[] pig_id_array;
    private List<String> mStrings_pig_id = new ArrayList<String>();
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<>();
    String date_n,getscore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_choose);

        getscore = getIntent().getExtras().getString("score");

        SharedPreferences farm = this.getSharedPreferences("Farm", Context.MODE_PRIVATE);
        farm_id = farm.getString("farm_id", "");
        unit_id = farm.getString("unit_id", "");
        unit_name = farm.getString("unit_name", "");
        farm_name = farm.getString("farm_name", "");

        txt_farm = findViewById(R.id.txt_farm);
        txt_unit = findViewById(R.id.txt_unit);
        spin_A1_1 = findViewById(R.id.spin_A1_1);
        btn_A1 = findViewById(R.id.btn_A1);
        edit_A1 = findViewById(R.id.edit_A1);
        result_bcs = findViewById(R.id.result_bcs);

        txt_farm.setText(farm_name);
        txt_unit.setText(unit_name);

        result_bcs.setFocusable(false);
        result_bcs.setText(getscore);

        //วันที่ปัจจุบัน
        date_n = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String url = "https://pigaboo.xyz/Query_pigid.php?farm_id="+farm_id+"&unit_id="+unit_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InsertBodyActivity.this, "ไม่สามารถดึงข้อมูลได้", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(stringRequest);




        btn_A1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences shared = getSharedPreferences("score", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.remove("score1");
                editor.commit();

                SharedPreferences shared2 = getSharedPreferences("score2", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor2 = shared2.edit();
                editor2.remove("score2");
                editor2.commit();

                new InsertAsyn().execute("https://pigaboo.xyz/Insert_bcsscore.php");
            }
        });
    }

    public void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            if (result.length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
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
                mStrings_pig_id.add(collectData.getString("pig_id"));
                pig_id_array = new String[mStrings_pig_id.size()];
                pig_id_array = mStrings_pig_id.toArray(pig_id_array);
                list.add(collectData.getString("pig_id"));

            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }

        listItems.addAll(list);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, listItems);
        spin_A1_1.setAdapter(adapter);
    }



    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {

                    OkHttpClient _okHttpClient = new OkHttpClient();
                    RequestBody _requestBody = new FormBody.Builder()
                            .add("pig_id", spin_A1_1.getSelectedItem().toString())
                            .add("event_recorddate", date_n)
                            .add("score", getscore)
                            .add("note", edit_A1.getText().toString())
                            .build();

                    Request _request = new Request.Builder().url(strings[0]).post(_requestBody).build();
                    _okHttpClient.newCall(_request).execute();

                return "successfully";
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(InsertBodyActivity.this, "บันทึกข้อมูลเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(InsertBodyActivity.this, Home.class);
                startActivity(i);
                finish();

            } else {
                Toast.makeText(InsertBodyActivity.this, "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(InsertBodyActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        builder.setCancelable(true);
        builder.setMessage("ยกเลิกการบันทึกข้อมูลใช่หรือไม่");
        builder.setPositiveButton("ใช่",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 = new Intent(InsertBodyActivity.this, HomeBCS.class);
                        startActivity(intent2);
                        finish();
                    }
                });
        builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }
}
