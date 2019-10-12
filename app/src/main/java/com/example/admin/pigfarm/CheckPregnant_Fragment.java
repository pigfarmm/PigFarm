package com.example.admin.pigfarm;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
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

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.AlgorithmConstraints;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class CheckPregnant_Fragment extends Fragment {
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;
    public static String gettextbreed, farm_id;
    Spinner spin_noteId02, spin_result02, spin_howtoresult;
    EditText edit_dateNote02;
    Button btn_flacAct02;
    ImageView img_calNote02;
    Calendar myCalendar = Calendar.getInstance();

    private String[] event_date_array;
    private String[] pig_id_array;
    private List<String> mStrings_pig_id = new ArrayList<String>();
    private List<String> mStrings_event_date = new ArrayList<String>();
    private int pig_id_dropdown;
    String getamount,item,d,m;


    public CheckPregnant_Fragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_act02, container, false);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        if (getArguments() != null) {
            gettextbreed = getArguments().getString("textbreed");
            farm_id = getArguments().getString("farm_id");
            Toast.makeText(getActivity(), gettextbreed, Toast.LENGTH_SHORT).show();
        }

        edit_dateNote02 = getView().findViewById(R.id.edit_dateNote02);
        spin_noteId02 = getView().findViewById(R.id.spin_noteId02);
        spin_result02 = getView().findViewById(R.id.spin_result02);
        btn_flacAct02 = getView().findViewById(R.id.btn_flacAct02);
        img_calNote02 = getView().findViewById(R.id.img_calNote02);
        spin_howtoresult = getView().findViewById(R.id.spin_howtoresult);

        String date_n = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());
        edit_dateNote02.setText(date_n);

        final String[] eventStr = getResources().getStringArray(R.array.checkpreg);
        final ArrayAdapter<String> adapterEvent = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, eventStr);
        spin_result02.setAdapter(adapterEvent);

        final String[] eventStr2 = getResources().getStringArray(R.array.howtocheckpreg);
        final ArrayAdapter<String> adapterEvent2 = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, eventStr2);
        spin_howtoresult.setAdapter(adapterEvent2);



        String url = "https://pigaboo.xyz/Query_BreedID.php?farm_id=" + farm_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                QueryBreedID(response);
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


        btn_flacAct02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url3 = "https://pigaboo.xyz/Query_AmountPregnantById.php?farm_id="+farm_id+"&pig_id="+spin_noteId02.getSelectedItem().toString();
                StringRequest stringRequest2 = new StringRequest(url3, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        QueryAmountPregnant(response);
                        new InsertAsyn().execute("https://pigaboo.xyz/Insert_EventCheckPregnant.php");
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

        spin_result02.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                item = parent.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        img_calNote02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
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
            edit_dateNote02.setText(year+"-"+m+"-"+d);
        }
    };

    public void QueryAmountPregnant(String response){
        try {
            JSONObject jsonObject3 = new JSONObject(response);
            JSONArray result3 = jsonObject3.getJSONArray("result");

            for (int i = 0; i<result3.length(); i++){
                JSONObject collectData3 = result3.getJSONObject(i);
                getamount = collectData3.getString("pig_amount_pregnant");
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            try {
                spin_noteId02.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        pig_id_dropdown = adapterView.getSelectedItemPosition();
                         Log.d("pig_123","value: "+pig_id_dropdown);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("event_id", "2")
                        .add("event_recorddate", edit_dateNote02.getText().toString())
                        .add("pig_id", pig_id_array[pig_id_dropdown])
                        .add("result_pregnant",item)
                        .add("note", event_date_array[pig_id_dropdown])
                        .add("pig_amount_pregnant",getamount)
                        .add("howtocheckpreg",spin_howtoresult.getSelectedItem().toString())
                        .build();

                Request _request = new Request.Builder().url(strings[0]).post(_requestBody).build();
                _okHttpClient.newCall(_request).execute();
                return "successfully";
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                Toast.makeText(getActivity(), "บันทึกข้อมูลเรียบร้อยแล้ว", Toast.LENGTH_SHORT).show();


            } else {
                Toast.makeText(getActivity(), "ไม่สามารถบันทึกข้อมูลได้", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void QueryBreedID(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject collectData = result.getJSONObject(i);
                mStrings_pig_id.add(collectData.getString("pig_id")) ;
                pig_id_array = new String[mStrings_pig_id.size()];
                pig_id_array = mStrings_pig_id.toArray(pig_id_array);
                list.add(collectData.getString("pig_id"));

                mStrings_event_date.add(collectData.getString("event_recorddate"));
                event_date_array = new String[mStrings_event_date.size()];
                event_date_array = mStrings_event_date.toArray(event_date_array);
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        listItems.addAll(list);
        adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_dropdown_item, listItems);
        spin_noteId02.setAdapter(adapter);
    }





}
