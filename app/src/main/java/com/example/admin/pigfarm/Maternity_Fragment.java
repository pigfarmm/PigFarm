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


public class Maternity_Fragment extends Fragment {

    String pig_amount_pregnant,sum_amount;
    public static String gettextbreed,farm_id;
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    Spinner spin_noteId04;
    EditText edit_dateNote04, edit_live04, edit_die04, edit_baby04, edit_weight04,edit_imgpro;
    Button btn_flacAct04;
    ImageView img_calNote04,img_process;
    private int amount_pregnant_index,amount;
    private String[] event_date_array;
    private List<String> mStrings_event_date = new ArrayList<String>();
    Calendar myCalendar = Calendar.getInstance();
    Date date_before,date_record;
    String m,d,getmaxeventid,unit_id;
    private String getsum_score;

    private String[] amount_pregnant_array;
    private String[] pig_id_array;
    private List<String> mStrings_pig_id = new ArrayList<String>();
    private List<String> mStrings_pregnant = new ArrayList<String>();

    public Maternity_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_act04, container, false);
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
            Toast.makeText(getActivity(), "คลอด", Toast.LENGTH_SHORT).show();
        }

        spin_noteId04 = getView().findViewById(R.id.spin_noteId04);
        edit_dateNote04 = getView().findViewById(R.id.edit_dateNote04);
        edit_live04 = getView().findViewById(R.id.edit_live04);
        edit_die04 = getView().findViewById(R.id.edit_die04);
        edit_baby04 = getView().findViewById(R.id.edit_baby04);
        edit_weight04 = getView().findViewById(R.id.edit_weight04);
        btn_flacAct04 = getView().findViewById(R.id.btn_flacAct04);
        img_calNote04 = getView().findViewById(R.id.img_calNote04);
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


        spin_noteId04.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adtv, View view, int i, long l) {
                amount_pregnant_index = adtv.getSelectedItemPosition();
                Log.d("amount_value","value: "+amount_pregnant_index);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String date_n = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());
        edit_dateNote04.setText(date_n);

        String url = "https://pigaboo.xyz/Query_Breed.php?farm_id=" + farm_id+"&unit_id="+unit_id;
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


        String url2 = "https://pigaboo.xyz/Query_AmountPregnant.php?farm_id="+farm_id+"&unit_id="+unit_id;
        StringRequest stringRequest2 = new StringRequest(url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                QueryAmountPregnant(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(this.getActivity().getApplicationContext());
        requestQueue2.add(stringRequest2);

        img_calNote04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        btn_flacAct04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (spin_noteId04.getSelectedItem() != null) {
                    String url3 = "https://pigaboo.xyz/Query_AmountPregnantById.php?farm_id="+farm_id+"&pig_id="+spin_noteId04.getSelectedItem().toString();
                    StringRequest stringRequest2 = new StringRequest(url3, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            QueryMaxEvent(response);
                            new InsertAsyn().execute("https://pigaboo.xyz/Insert_EventMaternity.php");
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

        img_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences shared = getActivity().getSharedPreferences("fragment_id", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("fragment_id","4");
                editor.commit();

                Intent intent = new Intent(getActivity(),HomeBCS.class);
                startActivity(intent);
            }
        });


    }

    private void QueryMaxEvent(String response) {
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
                date_before = formatter.parse(event_date_array[amount_pregnant_index]);
                date_record = formatter.parse(edit_dateNote04.getText().toString());
                int difference =  ((int)((date_record.getTime()/(24*60*60*1000)) - (int)(date_before.getTime()/(24*60*60*1000))));

                if (date_record.getTime() <= date_before.getTime()) {
                    getActivity().runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                                                        builder.setCancelable(false);
                                                        builder.setMessage("กรอกวันที่น้อยกว่าวันที่ผสมพันธุ์ไม่ได้");
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

                } if (difference < 114) {
                    getActivity().runOnUiThread(new Runnable() {
                                                    public void run() {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_Light_Dialog_Alert);
                                                        builder.setCancelable(false);
                                                        builder.setMessage("คลอดก่อนกำหนด 114 วัน");
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

                } if (!getmaxeventid.equals("1") && !getmaxeventid.equals("null") && !getmaxeventid.equals("17")){
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
                }
                else {

                    if (getsum_score == null){
                        getsum_score = "0";
                    }

                    amount = Integer.parseInt(amount_pregnant_array[amount_pregnant_index]);
                    amount = amount + 1;
                    OkHttpClient _okHttpClient = new OkHttpClient();
                    RequestBody _requestBody = new FormBody.Builder()
                            .add("event_id", "4")
                            .add("event_recorddate", edit_dateNote04.getText().toString())
                            .add("pig_id", spin_noteId04.getSelectedItem().toString())
                            .add("pig_alive", edit_live04.getText().toString())
                            .add("pig_die", edit_die04.getText().toString())
                            .add("pig_seedlings", edit_baby04.getText().toString())
                            .add("pig_allweight", edit_weight04.getText().toString())
                            .add("pig_amount_pregnant", String.valueOf(amount))
                            .add("bcs_score", edit_imgpro.getText().toString())
                            .build();

                    Request _request = new Request.Builder().url(strings[0]).post(_requestBody).build();
                    _okHttpClient.newCall(_request).execute();


                }
                return "successfully";
            }catch(IOException e){
                    e.printStackTrace();
                } catch(ParseException e){
                    e.printStackTrace();
                }
                return null;
                }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result == "successfully"){
                Toast.makeText(getActivity(), "บันทึกข้อมูลเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
                edit_live04.setText("");
                edit_die04.setText("");
                edit_baby04.setText("");
                edit_weight04.setText("");

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
                builder.setMessage("ยังไม่มีรายการบันทึกจากเหตุการณ์ผสมพันธุ์");
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

                mStrings_event_date.add(collectData.getString("event_recorddate"));
                event_date_array = new String[mStrings_event_date.size()];
                event_date_array = mStrings_event_date.toArray(event_date_array);

            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }

        listItems.addAll(list);
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, listItems);
        spin_noteId04.setAdapter(adapter);
    }




    public void QueryAmountPregnant(String response){
        try {
            JSONObject jsonObject2 = new JSONObject(response);
            JSONArray result2 = jsonObject2.getJSONArray("result");


            for (int i = 0; i<result2.length(); i++){
                JSONObject collectData2 = result2.getJSONObject(i);
                mStrings_pregnant.add(collectData2.getString("pig_amount_pregnant"));
                amount_pregnant_array = new String[mStrings_pregnant.size()];
                amount_pregnant_array = mStrings_pregnant.toArray(amount_pregnant_array);
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
            edit_dateNote04.setText(year+"-"+m+"-"+d);
        }
    };

    }

