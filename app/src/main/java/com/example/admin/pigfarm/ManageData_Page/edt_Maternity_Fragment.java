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
import com.example.admin.pigfarm.R;
import android.widget.Toast;

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

public class edt_Maternity_Fragment extends Fragment {

    String get_detail_id,getfarm_id,event_recorddate,event_name,pig_alive,pig_die,pig_seedlings,pig_allweight;
    EditText edt_eventname,edit_dateNote04,edit_live04,edit_die04,edit_baby04,edit_weight04;
    Button btn_flacAct04;
    ImageView img_calNote04;
    ProgressDialog progressDialog;
    ArrayList<String> listDad = new ArrayList<>();
    ArrayList<String> listItemsDad = new ArrayList<>();
    private String finalResult;
    private HttpParse httpParse = new HttpParse();
    ArrayAdapter<String> adapter;
    Calendar myCalendar = Calendar.getInstance();
    String UpdateURL = "https://pigaboo.xyz/Update_Event.php";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.edt_maternity_evt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences farm = this.getActivity().getSharedPreferences("Farm", Context.MODE_PRIVATE);
        getfarm_id = farm.getString("farm_id","");


        Bundle bundle2 = getArguments();
        get_detail_id = bundle2.getString("detail_id");

        edt_eventname = getView().findViewById(R.id.edt_eventname);
        edit_dateNote04 = getView().findViewById(R.id.edit_dateNote04);
        btn_flacAct04 = getView().findViewById(R.id.btn_flacAct04);
        btn_flacAct04 = getView().findViewById(R.id.btn_flacAct04);
        edit_live04 = getView().findViewById(R.id.edit_live04);
        edit_die04 = getView().findViewById(R.id.edit_die04);
        edit_baby04 = getView().findViewById(R.id.edit_baby04);
        edit_weight04 = getView().findViewById(R.id.edit_weight04);
        img_calNote04 = getView().findViewById(R.id.img_calNote04);

        img_calNote04.setOnClickListener(new View.OnClickListener() {
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
            edit_dateNote04.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
        }
    };

    private void LoadData() {
        String url = "https://pigaboo.xyz/Query_PigID_By_Detail_ID.php?detail_id="+get_detail_id;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                QueryDataMaternity(response);

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

    private void QueryDataMaternity(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            for (int i = 0; i < result.length(); i++) {
                JSONObject collectData = result.getJSONObject(i);

                event_recorddate = collectData.getString("event_recorddate");
                event_name = collectData.getString("event_name");
                pig_alive = collectData.getString("pig_alive");
                pig_die = collectData.getString("pig_die");
                pig_seedlings = collectData.getString("pig_seedlings");
                pig_allweight = collectData.getString("pig_allweight");

                edt_eventname.setText(event_name);
                edit_live04.setText(pig_alive);
                edit_die04.setText(pig_die);
                edit_baby04.setText(pig_seedlings);
                edit_weight04.setText(pig_allweight);
                edit_dateNote04.setText(event_recorddate);


            }

            btn_flacAct04.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    GetDataFromEditText();
                    update_data(event_recorddate, pig_alive,pig_die,pig_seedlings,pig_allweight,get_detail_id);
                }
            });


        }catch(JSONException ex){
            ex.printStackTrace();
        }
    }


    private void GetDataFromEditText() {
        event_recorddate = edit_dateNote04.getText().toString();
        pig_alive = edit_live04.getText().toString();
        pig_die = edit_die04.getText().toString();
        pig_seedlings = edit_baby04.getText().toString();
        pig_allweight = edit_weight04.getText().toString();

    }

    private void update_data(String event_recorddate, String pig_alive, String pig_die, String pig_seedlings, String pig_allweight, String get_detail_id) {

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
                hashMap.put("pig_alive",params[2]);
                hashMap.put("pig_die",params[3]);
                hashMap.put("pig_seedlings",params[4]);
                hashMap.put("pig_allweight",params[5]);


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
        update_dataclass.execute(get_detail_id,event_recorddate,pig_alive,pig_die,pig_seedlings,pig_allweight);

    }



}
