package com.example.admin.pigfarm.ManageData_Page;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;
import com.example.admin.pigfarm.R;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class edt_Exclude_Fragment extends Fragment {

    String get_detail_id,getfarm_id,event_recorddate,event_name,pig_resultofexclude,pig_reasonofexclude,getunit_id,bcs_score;
    EditText edit_eventname,edit_dateNote08,edit_desc08,edit_imgpro;
    Spinner spin_result08,spin_result09;
    Button btn_flacAct08;
    ImageView img_calNote08;
    ArrayList<String> listDad = new ArrayList<>();
    ArrayList<String> listItemsDad = new ArrayList<>();
    private String finalResult;
    ProgressDialog progressDialog;
    private HttpParse httpParse = new HttpParse();
    ArrayAdapter<String> adapter;
    Calendar myCalendar = Calendar.getInstance();
    String UpdateURL = "https://pigaboo.xyz/Update_Event.php";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edt_exclude_evt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences farm = this.getActivity().getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_id = farm.getString("farm_id","");
        getunit_id = farm.getString("unit_id","");


        Bundle bundle2 = getArguments();
        get_detail_id = bundle2.getString("detail_id");

        edit_eventname = getView().findViewById(R.id.edit_eventname);
        edit_dateNote08 = getView().findViewById(R.id.edit_dateNote08);
        btn_flacAct08 = getView().findViewById(R.id.btn_flacAct08);
        img_calNote08 = getView().findViewById(R.id.img_calNote08);
        spin_result08 = getView().findViewById(R.id.spin_result08);
        spin_result09 = getView().findViewById(R.id.spin_result09);
        edit_imgpro = getView().findViewById(R.id.edit_imgpro);

        final String[] eventStr = getResources().getStringArray(R.array.exclude_event);
        ArrayAdapter<String> adapterEvent = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_dropdown_item_1line, eventStr);
        spin_result08.setAdapter(adapterEvent);

        final String[] eventStr2 = getResources().getStringArray(R.array.cause_exclude_event);
        ArrayAdapter<String> adapterEvent2 = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_dropdown_item, eventStr2);
        spin_result09.setAdapter(adapterEvent2);

        img_calNote08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        LoadData();
    }

    private void showDatePickerDialog() {
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
            edit_dateNote08.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
        }
    };

    private void LoadData() {
        String url = "https://pigaboo.xyz/Query_PigID_By_Detail_ID.php?detail_id="+get_detail_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                QueryDataExclude(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity().getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext().getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void QueryDataExclude(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject collectData = result.getJSONObject(i);

                event_recorddate = collectData.getString("event_recorddate");
                event_name = collectData.getString("event_name");
                pig_resultofexclude = collectData.getString("pig_resultofexclude");
                pig_reasonofexclude = collectData.getString("pig_reasonofexclude");
                bcs_score = collectData.getString("bcs_score");


                edit_eventname.setText(event_name);
                edit_dateNote08.setText(event_recorddate);
                edit_desc08.setText(pig_reasonofexclude);
                edit_imgpro.setText(bcs_score);


            }

            btn_flacAct08.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GetDataFromEditText();
                    update_data(event_recorddate,pig_reasonofexclude,pig_resultofexclude,get_detail_id,bcs_score);
                }
            });


        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }

    private void GetDataFromEditText() {
        event_recorddate = edit_dateNote08.getText().toString();
        pig_reasonofexclude = spin_result09.getSelectedItem().toString();
        pig_resultofexclude = spin_result08.getSelectedItem().toString();
        bcs_score = edit_imgpro.getText().toString();
    }


    private void update_data(String event_recorddate, String pig_reasonofexclude,String pig_resultofexclude, String get_detail_id,String bcs_score) {
        class update_dataClass extends AsyncTask<String,Void,String> {

            @Override
            protected void onPreExecute() {
                progressDialog = ProgressDialog.show(getActivity(),"กำลังอัพเดตข้อมูล...",null,true,true);
                super.onPreExecute();

            }

            @Override
            protected String doInBackground(String... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("detail_id",params[0]);
                hashMap.put("event_recorddate",params[1]);
                hashMap.put("pig_reasonofexclude",params[2]);
                hashMap.put("pig_resultofexclude",params[3]);
                hashMap.put("bcs_score",params[4]);


                finalResult = httpParse.postRequest(hashMap,UpdateURL);
                return finalResult;
            }

            @Override
            protected void onPostExecute(String httpResponseMsg) {
                super.onPostExecute(httpResponseMsg);
                progressDialog.dismiss();
                Toast.makeText(getActivity(),httpResponseMsg.toString(), Toast.LENGTH_LONG).show();

            }
        }

        update_dataClass update_dataclass = new update_dataClass();
        update_dataclass.execute(get_detail_id,event_recorddate,pig_reasonofexclude,pig_resultofexclude,bcs_score);


    }
    }

