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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import java.util.List;
import java.util.Locale;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class Pregnent_Fragment extends Fragment {
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    public static String gettextbreed,farm_id,getamount;
    Spinner spin_noteId03;
    EditText edit_dateNote03,edit_msg03,edit_imgpro;
    Button btn_flacAct03;
    private String[] event_date_array;
    private String[] pig_id_array;
    private List<String> mStrings_pig_id = new ArrayList<String>();
    private List<String> mStrings_event_date = new ArrayList<String>();
    private int pig_id_dropdown;
    ImageView img_calNote03,img_process;
    Calendar myCalendar = Calendar.getInstance();
    String d,m,unit_id,getmaxeventid,event_recorddate;
    private String getsum_score;



    public Pregnent_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_act03, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences farm = this.getActivity().getSharedPreferences("Farm", Context.MODE_PRIVATE);
        unit_id = farm.getString("unit_id", "");

        if (getArguments() != null){
            gettextbreed = getArguments().getString("textbreed");
            farm_id = getArguments().getString("farm_id");
            getsum_score = getArguments().getString("sum_score");
            Toast.makeText(getActivity(), "แท้ง", Toast.LENGTH_SHORT).show();
        }

            spin_noteId03 = getView().findViewById(R.id.spin_noteId03);
            edit_dateNote03 = getView().findViewById(R.id.edit_dateNote03);
            edit_msg03 = getView().findViewById(R.id.edit_msg03);
            btn_flacAct03 = getView().findViewById(R.id.btn_flacAct03);
            img_calNote03 = getView().findViewById(R.id.img_calNote03);
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
        edit_dateNote03.setText(date_n);

        String url = "https://pigaboo.xyz/Query_pigid.php?farm_id="+farm_id+"&unit_id="+unit_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        requestQueue.add(stringRequest);


            btn_flacAct03.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url3 = "https://pigaboo.xyz/Query_AmountPregnantById.php?farm_id="+farm_id+"&pig_id="+spin_noteId03.getSelectedItem().toString();
                    StringRequest stringRequest2 = new StringRequest(url3, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            QueryAmountPregnant(response);

                            String url4 = "https://pigaboo.xyz/Query_BreedIDForPregnant.php?farm_id=" + farm_id+"&unit_id="+unit_id+"&pig_id="+spin_noteId03.getSelectedItem().toString();
                            StringRequest stringRequest = new StringRequest(url4, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    QueryAbort(response);
                                    new InsertAsyn().execute("https://pigaboo.xyz/Insert_EventPregnant.php");
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), "ไม่สามารถดึงข้อมูลได้", Toast.LENGTH_SHORT).show();
                                }
                            }
                            );
                            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                            requestQueue.add(stringRequest);



                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                    );
                    RequestQueue requestQueue2 = Volley.newRequestQueue(getActivity().getApplicationContext());
                    requestQueue2.add(stringRequest2);

                }
            });

        img_calNote03.setOnClickListener(new View.OnClickListener() {
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
                editor.putString("fragment_id","3");
                editor.commit();

                Intent intent = new Intent(getActivity(),HomeBCS.class);
                startActivity(intent);
            }
        });
    }

    private void showJSON(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");


            for (int i = 0; i<result.length(); i++){
                JSONObject collectData = result.getJSONObject(i);
                list.add(collectData.getString("pig_id"));
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }

        listItems.addAll(list);
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, listItems);
        spin_noteId03.setAdapter(adapter);




    }

    private void QueryAmountPregnant(String response) {
        try {
            JSONObject jsonObject3 = new JSONObject(response);
            JSONArray result3 = jsonObject3.getJSONArray("result");

            for (int i = 0; i<result3.length(); i++){
                JSONObject collectData3 = result3.getJSONObject(i);

                if (collectData3.getString("max_eventid") == null){
                    getmaxeventid = "0";
                }else{
                    getmaxeventid = collectData3.getString("max_eventid");
                }

                if (collectData3.getString("pig_amount_pregnant") == null){
                    getamount = "0";
                }else{
                    getamount = collectData3.getString("pig_amount_pregnant");
                }

            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }


    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                spin_noteId03.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        pig_id_dropdown = adapterView.getSelectedItemPosition();
                        Log.d("pig_123","value: "+pig_id_dropdown);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                if (!getmaxeventid.equals("1") && !getmaxeventid.equals("null") && !getmaxeventid.equals("17")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                            builder1.setCancelable(false);
                            builder1.setMessage("เหตุการณ์ล่าสุดไม่ใช่เหตุการณ์ผสม");
                            builder1.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            AlertDialog dialog = builder1.create();
                            dialog.show();
                        }
                    });
                    return "not success";
                }else{
                    if (getsum_score == null){
                        getsum_score = "0";
                    }

                    OkHttpClient _okHttpClient = new OkHttpClient();
                    RequestBody _requestBody = new FormBody.Builder()
                            .add("event_id", "3")
                            .add("event_recorddate", edit_dateNote03.getText().toString())
                            .add("pig_id", spin_noteId03.getSelectedItem().toString())
                            .add("note", edit_msg03.getText().toString())
                            .add("event_recorddate_for_abort", event_recorddate)
                            .add("pig_amount_pregnant", getamount)
                            .add("bcs_score", edit_imgpro.getText().toString())
                            .build();

                    Request _request = new Request.Builder().url(strings[0]).post(_requestBody).build();
                    _okHttpClient.newCall(_request).execute();
                    return "successfully";
                }
            }catch(IOException e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == "successfully"){
                Toast.makeText(getActivity(), "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
                edit_msg03.setText("");

            }else {
                Toast.makeText(getActivity(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void QueryAbort(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");


            for (int i = 0; i < result.length(); i++) {
                JSONObject collectData = result.getJSONObject(i);

                event_recorddate = collectData.getString("event_recorddate");

            }

        }catch (JSONException ex) {
            ex.printStackTrace();
        }

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
            edit_dateNote03.setText(year+"-"+m+"-"+d);
        }
    };
}
