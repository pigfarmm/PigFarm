package com.example.admin.pigfarm;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
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


public class Breed_Fragment extends Fragment {
    Spinner spin_noteId01,spin_dadId01;
    EditText edit_dateNote01;
    Button btn_flacAct01;
    private String record_date,recorddate,getamount,getmaxeventid,max_count,event_recorddate,group_rut,m,d,unit_id;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listDad = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<String> listItemsDad = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;
    public static String gettextbreed,farm_id;
    ImageView img_calNote01;
    Calendar myCalendar = Calendar.getInstance();
    Date date_before,date_record,date_indb,date_infield;
    Object pregnant;
    private String[] event_date_array;
    private String[] pig_id_array;
    private List<String> mStrings_pig_id = new ArrayList<String>();
    private List<String> mStrings_event_date = new ArrayList<String>();
    private String[] amount_pregnant_array;
    private List<String> mStrings_pregnant = new ArrayList<String>();
    private int pig_id_dropdown,amount,sum_count,sum_grouprut;
    private List<String> sum_amount_pregnant = new ArrayList<String>();

    public Breed_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.frag_act01, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences farm = this.getActivity().getSharedPreferences("Farm", Context.MODE_PRIVATE);
        unit_id = farm.getString("unit_id", "");

        if (getArguments() != null) {
            gettextbreed = getArguments().getString("textbreed");
            farm_id = getArguments().getString("farm_id");
            Toast.makeText(getActivity(), gettextbreed, Toast.LENGTH_SHORT).show();
        }

        spin_noteId01 = getView().findViewById(R.id.spin_noteId01);
        spin_dadId01 = getView().findViewById(R.id.spin_dadId01);
        edit_dateNote01 = getView().findViewById(R.id.edit_dateNote01);
        btn_flacAct01 = getView().findViewById(R.id.btn_flacAct01);
        img_calNote01 = getView().findViewById(R.id.img_calNote01);

        String date_n = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        edit_dateNote01.setText(date_n);



            String url = "https://pigaboo.xyz/Query_pigid.php?farm_id="+farm_id+"&unit_id="+unit_id;
            StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    showJSON(response);
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



            String url2 = "https://pigaboo.xyz/Query_DadID.php?farm_id="+farm_id+"&unit_id="+unit_id;
            StringRequest stringRequest1 = new StringRequest(url2, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    QueryDadID(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), "ไม่สามารถดึงข้อมูลได้", Toast.LENGTH_SHORT).show();
                }
            }
            );
            RequestQueue requestQueue1 = Volley.newRequestQueue(this.getActivity().getApplicationContext());
            requestQueue1.add(stringRequest1);



        btn_flacAct01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spin_dadId01 != null && spin_dadId01.getSelectedItem() != null) {
                    String url3 = "https://pigaboo.xyz/Query_AmountPregnantById.php?farm_id="+farm_id+"&pig_id="+spin_noteId01.getSelectedItem().toString();
                    StringRequest stringRequest2 = new StringRequest(url3, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            QueryAmountPregnant(response);
                            Rut();
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
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder.setCancelable(false);
                    builder.setMessage("กรุณากรอกข้อมูลให้ครบถ้วน");
                    builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });



        img_calNote01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        spin_noteId01.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                pig_id_dropdown = adapterView.getSelectedItemPosition();
                Log.d("pig_123","value: "+pig_id_dropdown);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

    }

    private void Rut(){
    String url4 = "https://pigaboo.xyz/Query_CountRut.php?farm_id="+farm_id+"&pig_id="+spin_noteId01.getSelectedItem().toString();
    StringRequest stringRequest3 = new StringRequest(url4, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            QueryCountRut(response);
            new InsertAsyn().execute("https://pigaboo.xyz/Insert_EventBreed.php");
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
        }
    }
    );
    RequestQueue requestQueue3 = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue3.add(stringRequest3);
}

    private void QueryCountRut(String response) {
        try {
            JSONObject jsonObject4 = new JSONObject(response);
            JSONArray result4 = jsonObject4.getJSONArray("result");

            for (int i = 0; i<result4.length(); i++){
                JSONObject collectData4 = result4.getJSONObject(i);
                if (collectData4.getString("max_count") == "null"){
                    max_count = "0";
                }else{
                    max_count = collectData4.getString("max_count");
                }
                if (collectData4.getString("event_recorddate") == null){
                    event_recorddate = edit_dateNote01.getText().toString();
                }else{
                    event_recorddate = collectData4.getString("event_recorddate");
                }

                if (collectData4.getString("group_rut") == null){
                    group_rut = "0";
                }else{
                    group_rut = collectData4.getString("group_rut");
                }

                Log.d("result max_date","value: " + collectData4.getString("event_recorddate"));
                Log.d("result max_count_rut","value: " + max_count);
                Log.d("result group_rut","value: " + group_rut);
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

            edit_dateNote01.setText(year+"-"+m+"-"+d);
        }
    };

    public void QueryAmountPregnant(String response){
        try {
            JSONObject jsonObject3 = new JSONObject(response);
            JSONArray result3 = jsonObject3.getJSONArray("result");

            for (int i = 0; i<result3.length(); i++){
                JSONObject collectData3 = result3.getJSONObject(i);
                if (collectData3.getString("pig_amount_pregnant") == null){
                    getamount = "0";
                }else{
                    getamount = collectData3.getString("pig_amount_pregnant");

                }if (collectData3.getString("max_eventid") == null){
                    getmaxeventid = "0";
                }else{
                    getmaxeventid = collectData3.getString("max_eventid");
                }

                Log.d("result getmaxeventid","value: "+getmaxeventid);
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }


    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {

                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                date_before = formatter.parse(event_date_array[pig_id_dropdown]);
                date_record = formatter.parse(edit_dateNote01.getText().toString());

                if (date_record.getTime() < date_before.getTime()) {
                    getActivity().runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                                                        builder.setCancelable(false);
                                                        builder.setMessage("กรอกวันที่น้อยกว่าวันที่เข้าฟาร์มไม่ได้");
                                                        builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                dialog.cancel();
                                                            }
                                                        });
                                                        AlertDialog dialog = builder.create();
                                                        dialog.show();
                                                    }
                                                }
                    );
                    return null;
                }
                if (!getmaxeventid.equals("6") && !getmaxeventid.equals("1") && !getmaxeventid.equals("null") && !getmaxeventid.equals("3")){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                            builder1.setCancelable(false);
                            builder1.setMessage("เหตุการณ์ล่าสุดไม่ใช่เหตุการณ์ผสม หรือ หย่านม");
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
                }
                else{
                    OkHttpClient _okHttpClient = new OkHttpClient();
                    RequestBody _requestBody = new FormBody.Builder()
                            .add("event_id", "1")
                            .add("event_recorddate", edit_dateNote01.getText().toString())
                            .add("pig_id", spin_noteId01.getSelectedItem().toString())
                            .add("pig_breeder", spin_dadId01.getSelectedItem().toString())
                            .add("pig_amount_pregnant", getamount)
                            .build();

                    Request _request = new Request.Builder().url(strings[0]).post(_requestBody).build();
                    _okHttpClient.newCall(_request).execute();

                }
                return "successfully";
            }catch(IOException e){
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("result async","value: "+s);
            if (s == "successfully"){
                new InsertRut().execute("https://pigaboo.xyz/Insert_Rut.php");
            }else {
                Toast.makeText(getActivity(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }


       public void showJSON(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            if (result.length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
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

                mStrings_event_date.add(collectData.getString("pig_recorddate"));
                event_date_array = new String[mStrings_event_date.size()];
                event_date_array = mStrings_event_date.toArray(event_date_array);
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }

        listItems.addAll(list);
           adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, listItems);
           spin_noteId01.setAdapter(adapter);
    }



    public void QueryDadID(String response){
        try {
            JSONObject jsonObject2 = new JSONObject(response);
            JSONArray result2 = jsonObject2.getJSONArray("result");

            if (result2.length() == 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
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

        listItemsDad.addAll(listDad);
        adapter1 = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, listItemsDad);
        spin_dadId01.setAdapter(adapter1);
    }


    private class InsertRut extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                if (event_recorddate == null){
                    event_recorddate = edit_dateNote01.getText().toString();
                }else{
                    event_recorddate = event_recorddate.toString();
                }if (max_count == null){
                    max_count = "0";
                }if (group_rut == null){
                    group_rut = "0";
                }

                DateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd");

                date_indb =  formatter2.parse(event_recorddate);
                date_infield = formatter2.parse(edit_dateNote01.getText().toString());

                long diff = date_infield.getTime() - date_indb.getTime();
                long seconds = diff / 1000;
                long minutes = seconds / 60;
                long hours = minutes / 60;
                long days = hours / 24;

                if (days >= 11) {
                    sum_count = 0;
                    group_rut = group_rut.toString();
                    sum_grouprut = Integer.parseInt(group_rut)+1;
                }else{
                    group_rut = group_rut.toString();
                    max_count = max_count.toString();
                    sum_count = Integer.parseInt(max_count);
                    sum_grouprut = Integer.parseInt(group_rut);
                }

                Log.d("compare","value :"+days);

                OkHttpClient _okHttpClient2 = new OkHttpClient();
                RequestBody _requestBody2 = new FormBody.Builder()
                        .add("event_id", "1")
                        .add("event_recorddate", edit_dateNote01.getText().toString())
                        .add("pig_id", spin_noteId01.getSelectedItem().toString())
                        .add("pig_amount_pregnant", getamount)
                        .add("count_rut", String.valueOf(sum_count+1))
                        .add("group_rut", String.valueOf(sum_grouprut))
                        .build();

                Request _request = new Request.Builder().url(strings[0]).post(_requestBody2).build();
                _okHttpClient2.newCall(_request).execute();
                return "successfully";
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("result async","value: "+s);
            if (s == "successfully"){
                Toast.makeText(getActivity(), "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getActivity(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
