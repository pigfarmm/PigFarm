package com.example.admin.pigfarm;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.admin.pigfarm.BodyAnalyze.HomeBCS;
import com.example.admin.pigfarm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Rutnotbreeded_Fragment extends Fragment {
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    EditText edit_dateNote15,edit_msg15,edit_imgpro;
    Spinner spin_noteId15;
    ImageView img_calNote15,img_process;
    Button btn_flacAct15;
    Calendar myCalendar = Calendar.getInstance();
    public static String gettextbreed,farm_id,m,d;
    String unit_id;
    private String getsum_score;

    public Rutnotbreeded_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_act15, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences farm = this.getActivity().getSharedPreferences("Farm", Context.MODE_PRIVATE);
        unit_id = farm.getString("unit_id", "");

        if (getArguments() != null){
            String gettextbreed = getArguments().getString("textbreed");
            farm_id = getArguments().getString("farm_id");
            getsum_score = getArguments().getString("sum_score");
            Toast.makeText(getActivity(), "เป็นสัดแต่ไม่ผสมพันธุ์", Toast.LENGTH_SHORT).show();
        }

        edit_dateNote15 = getView().findViewById(R.id.edit_dateNote15);
        edit_msg15 = getView().findViewById(R.id.edit_msg15);
        spin_noteId15 = getView().findViewById(R.id.spin_noteId15);
        img_calNote15 = getView().findViewById(R.id.img_calNote15);
        btn_flacAct15 = getView().findViewById(R.id.btn_flacAct15);
        img_process = getView().findViewById(R.id.img_process);
        edit_imgpro = getView().findViewById(R.id.edit_imgpro);

        if (getsum_score != null){
            edit_imgpro.setText(getsum_score);
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
            builder.setCancelable(false);
            builder.setMessage("หากท่านผู้ใช้งานต้องการใช้งานฟังก์ชั่น 'ประเมินหุ่นสุกร' โปรดทำการประเมินหุุ่นสุกรก่อนทำการกรอกรายละเอียดอื่นๆ");
            builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

            edit_imgpro.setText("");
        }

        String date_n = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());

        edit_dateNote15.setText(date_n);

        String url = "https://pigaboo.xyz/Query_WeanID.php?farm_id="+farm_id+"&unit_id="+unit_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Query_Rutnotbreeded(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "ไม่สามารถดึงข้อมูลได้", Toast.LENGTH_SHORT).show();
            }

        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        requestQueue.add(stringRequest);

        btn_flacAct15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InsertAsyn().execute("https://pigaboo.xyz/Insert_EventRutnotbreed.php");
            }
        });

        img_calNote15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        img_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences shared = getActivity().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("fragment_id","11");
                editor.commit();

                Intent intent = new Intent(getActivity(),HomeBCS.class);
                startActivity(intent);
            }
        });
    }

    public void showDatePickerDialog(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
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
            edit_dateNote15.setText(year+"-"+m+"-"+d);
        }
    };

    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                if (getsum_score == null){
                    getsum_score = "0";
                }

                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("event_id", "12")
                        .add("event_recorddate", edit_dateNote15.getText().toString())
                        .add("pig_id", spin_noteId15.getSelectedItem().toString())
                        .add("note", edit_msg15.getText().toString())
                        .add("bcs_score", edit_imgpro.getText().toString())
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

            if (result != null){
                Toast.makeText(getActivity(), "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
                edit_msg15.setText("");
                edit_imgpro.setText("");
            }else {
                Toast.makeText(getActivity(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Query_Rutnotbreeded(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            if (result.length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                builder.setCancelable(false);
                builder.setMessage("ยังไม่มีรายการบันทึกจากเหตุการณ์หย่านม");
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
                list.add(collectData.getString("pig_id"));
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }


        listItems.addAll(list);
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, listItems);
        spin_noteId15.setAdapter(adapter);
    }


}

