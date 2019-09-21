package com.example.admin.pigfarm;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
    EditText edit_dateNote04, edit_live04, edit_die04, edit_baby04, edit_weight04;
    Button btn_flacAct04;
    ImageView img_calNote04;
    private int amount_pregnant_index,amount;
    private String[] event_date_array;
    private List<String> mStrings_event_date = new ArrayList<String>();
    Calendar myCalendar = Calendar.getInstance();
    Date date_before,date_record;

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
        if (getArguments() != null){
            gettextbreed = getArguments().getString("textbreed");
            farm_id = getArguments().getString("farm_id");
            Toast.makeText(getActivity(), gettextbreed, Toast.LENGTH_SHORT).show();
        }

        spin_noteId04 = getView().findViewById(R.id.spin_noteId04);
        edit_dateNote04 = getView().findViewById(R.id.edit_dateNote04);
        edit_live04 = getView().findViewById(R.id.edit_live04);
        edit_die04 = getView().findViewById(R.id.edit_die04);
        edit_baby04 = getView().findViewById(R.id.edit_baby04);
        edit_weight04 = getView().findViewById(R.id.edit_weight04);
        btn_flacAct04 = getView().findViewById(R.id.btn_flacAct04);
        img_calNote04 = getView().findViewById(R.id.img_calNote04);

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

        String url = "https://pigaboo.xyz/Query_BreedID.php?farm_id="+farm_id;
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


        String url2 = "https://pigaboo.xyz/Query_AmountPregnant.php?farm_id="+farm_id;
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
//                Toast.makeText(getActivity(), amount_pregnant_array[amount_pregnant_index], Toast.LENGTH_SHORT).show();
                new InsertAsyn().execute("https://pigaboo.xyz/Insert_EventMaternity.php");
            }
        });


    }

    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                date_before = formatter.parse(event_date_array[amount_pregnant_index]);
                date_record = formatter.parse(edit_dateNote04.getText().toString());

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

                } else {
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

            if (result != null){
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
            edit_dateNote04.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
        }
    };

    }

