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


public class Piggydead_Fragment extends Fragment {

    public static String gettextbreed,farm_id;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner spin_noteId11,spin_result08;
    EditText edit_dateNote11, edit_count11,edit_cause11,edit_imgpro;
    Button btn_flacAct11;
    ImageView img_calNote11,img_process;
    String getamount,m,d,unit_id;
    Calendar myCalendar = Calendar.getInstance();
    private String getsum_score,getmaxeventid;


    public Piggydead_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_act11, container, false);
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
            Toast.makeText(getActivity(), "ลูกหมูตาย", Toast.LENGTH_SHORT).show();
        }

        spin_noteId11 = getView().findViewById(R.id.spin_noteId11);
        edit_dateNote11 = getView().findViewById(R.id.edit_dateNote11);
        edit_count11 = getView().findViewById(R.id.edit_count11);
        btn_flacAct11 = getView().findViewById(R.id.btn_flacAct11);
        img_calNote11 = getView().findViewById(R.id.img_calNote11);
        edit_cause11 = getView().findViewById(R.id.edit_cause11);
        spin_result08 = getView().findViewById(R.id.spin_result08);
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
        edit_dateNote11.setText(date_n);

        final String[] eventStr = getResources().getStringArray(R.array.piggydead_event);
        ArrayAdapter<String> adapterEvent = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, eventStr);
        spin_result08.setAdapter(adapterEvent);

        String url = "https://pigaboo.xyz/Query_Maternity.php?farm_id="+farm_id+"&unit_id="+unit_id;
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

        btn_flacAct11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spin_noteId11 != null && spin_noteId11.getSelectedItem() != null) {
                    String url3 = "https://pigaboo.xyz/Query_AmountPregnantById.php?farm_id=" + farm_id + "&pig_id=" + spin_noteId11.getSelectedItem().toString();
                    StringRequest stringRequest2 = new StringRequest(url3, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            QueryAmountPregnant(response);
                            new InsertAsyn().execute("https://pigaboo.xyz/Insert_EventPiggyDead.php");
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
                }else{
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

        img_calNote11.setOnClickListener(new View.OnClickListener() {
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
                editor.putString("fragment_id","10");
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
            edit_dateNote11.setText(year+"-"+m+"-"+d);
        }
    };

    public void QueryAmountPregnant(String response){
        try {
            JSONObject jsonObject3 = new JSONObject(response);
            JSONArray result3 = jsonObject3.getJSONArray("result");

            for (int i = 0; i<result3.length(); i++){
                JSONObject collectData3 = result3.getJSONObject(i);
                getamount = collectData3.getString("pig_amount_pregnant");
                getmaxeventid = collectData3.getString("max_eventid");
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
                            protected String doInBackground(String... strings) {
                                try{

                                    if (!getmaxeventid.equals("4") && !getmaxeventid.equals("17")){
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                                                builder1.setCancelable(false);
                                                builder1.setMessage("เหตุการณ์ล่าสุดไม่ใช่เหตุการณ์คลอด");
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
                            .add("event_id", "11")
                            .add("event_recorddate", edit_dateNote11.getText().toString())
                            .add("pig_id", spin_noteId11.getSelectedItem().toString())
                            .add("note", spin_result08.getSelectedItem().toString())
                            .add("pig_dieafter", edit_count11.getText().toString())
                            .add("pig_amount_pregnant",getamount)
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

            if (result != null){
                Toast.makeText(getActivity(), "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
                edit_count11.setText("");
                edit_imgpro.setText("");

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
                builder.setMessage("ท่านยังไม่ได้บันทึกเหตุการณ์คลอด");
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
        spin_noteId11.setAdapter(adapter);
    }

}
