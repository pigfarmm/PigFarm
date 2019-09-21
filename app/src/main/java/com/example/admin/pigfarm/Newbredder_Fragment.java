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
import com.example.admin.pigfarm.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

public class Newbredder_Fragment extends Fragment {

    EditText edit_id3, edit_opendate3, edit_birthday3, edit_breed3, edit_dadId3, edit_momId3, edit_form3, edit_reserveID3;
    Button btn_saveBio3;
    String farm_id;
    ImageView img_calOpen3, img_calBD3,qr_code2;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar2 = Calendar.getInstance();
    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<String> listItemsDad = new ArrayList<>();
    ArrayList<String> listDad = new ArrayList<>();
    ArrayAdapter<String> adapter1;



    public Newbredder_Fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bredder, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences shared = getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        farm_id = shared.getString("farm_id", "missing");


        bindWidget();

        qr_code2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(Newbredder_Fragment.this).initiateScan();
                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Please focus the camera on the QR Code");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(scanResult != null){
            edit_id3.setText(scanResult.getContents());
        }
    }

    private void bindWidget() {
        edit_id3 = getView().findViewById(R.id.edit_id3);
        edit_opendate3 = getView().findViewById(R.id.edit_opendate3);
        edit_birthday3= getView().findViewById(R.id.edit_birthday3);
        edit_breed3 = getView().findViewById(R.id.edit_breed3);
        edit_momId3 = getView().findViewById(R.id.edit_momId3);
        edit_form3 = getView().findViewById(R.id.edit_form3);
        edit_reserveID3 = getView().findViewById(R.id.edit_reserveID3);
        btn_saveBio3 = getView().findViewById(R.id.btn_saveBio3);
        img_calOpen3 = getView().findViewById(R.id.img_calOpen3);
        img_calBD3 = getView().findViewById(R.id.img_calBD3);
        edit_dadId3= getView().findViewById(R.id.edit_dadId3);
        qr_code2 = getView().findViewById(R.id.img_qr2);

        String date_n = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());
        edit_opendate3.setText(date_n);

        btn_saveBio3.setOnClickListener(onSubmitClickListener);

        img_calOpen3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        img_calBD3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2();
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
            edit_opendate3.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
        }
    };

    public void showDatePickerDialog2(){

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener date2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar2.set(Calendar.YEAR, year);
            myCalendar2.set(Calendar.MONTH, monthOfYear);
            myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            monthOfYear = monthOfYear + 1;
            edit_birthday3.setText(year+"-"+monthOfYear+"-"+dayOfMonth);
        }
    };

    private View.OnClickListener onSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new InsertAsyn().execute("https://pigaboo.xyz/Insert_ProfilePig.php");

        }
    };

    private class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("pig_id", edit_id3.getText().toString())
                        .add("pig_recorddate", edit_opendate3.getText().toString())
                        .add("pig_birthday", edit_birthday3.getText().toString())
                        .add("pig_breed", edit_breed3.getText().toString())
                        .add("pig_idbreeder", edit_dadId3.getText().toString())
                        .add("pig_idbreeder2", edit_momId3.getText().toString())
                        .add("pig_from", edit_form3.getText().toString())
                        .add("pig_idreserve", edit_reserveID3.getText().toString())
                        .add("farm_id",farm_id)
                        .add("idrecordtype_pig","3")
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
                edit_id3.setText("");
                edit_opendate3.setText("");
                edit_birthday3.setText("");
                edit_breed3.setText("");
                edit_dadId3.setText("");
                edit_momId3.setText("");
                edit_form3.setText("");
                edit_reserveID3.setText("");
            }else {
                Toast.makeText(getActivity(), "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
