package com.example.admin.pigfarm;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
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
import com.example.admin.pigfarm.SelectDateFragment;
import com.google.zxing.Result;
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

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class Newpig_Fragment extends Fragment {

    Button btnSubmitNewpig;
    EditText edit_id, edit_opendate, edit_birthday, edit_breed, edit_dadId1, edit_momId, edit_form, edit_reserveID,id;
    String farm_id,m,d,m2,d2,unit_id;
    ImageView img_calOpen1, img_calBD1,qr_code;
    Calendar myCalendar = Calendar.getInstance();
    Calendar myCalendar2 = Calendar.getInstance();
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> listDad = new ArrayList<>();
    ArrayList<String> listItems = new ArrayList<>();
    ArrayList<String> listItemsDad = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter1;
    private ZXingScannerView zXingScannerView;

    public Newpig_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_newpig, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences shared = getActivity().getSharedPreferences("Farm", Context.MODE_PRIVATE);
        farm_id = shared.getString("farm_id", "");
        unit_id = shared.getString("unit_id", "");


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA},
                    50); }

        edit_id = getView().findViewById(R.id.edit_id1);
        edit_opendate = getActivity().findViewById(R.id.edit_opendate1);
        edit_birthday = getView().findViewById(R.id.edit_birthday1);
        edit_breed = getView().findViewById(R.id.edit_breed1);
        edit_momId = getView().findViewById(R.id.edit_momId1);
        edit_form = getView().findViewById(R.id.edit_form1);
        edit_reserveID = getView().findViewById(R.id.edit_reserveID);
        btnSubmitNewpig = getView().findViewById(R.id.btn_youngpig);
        img_calOpen1 = getView().findViewById(R.id.img_calOpen1);
        img_calBD1 = getView().findViewById(R.id.img_calBD1);
        edit_dadId1 = getView().findViewById(R.id.edit_dadId1);
        qr_code = getView().findViewById(R.id.img_qr);



        String date_n = new SimpleDateFormat("yyyy-MM-dd",
                Locale.getDefault()).format(new Date());
        edit_opendate.setText(date_n);


        btnSubmitNewpig.setOnClickListener(onSubmitClickListener);

        img_calOpen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        img_calBD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog2();
            }
        });

        qr_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(Newpig_Fragment.this).initiateScan();
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
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(scanResult != null){
            edit_id.setText(scanResult.getContents());
        }

    }

    public void showDatePickerDialog() {

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
            edit_opendate.setText(year+"-"+m+"-"+d);
        }
    };

    public void showDatePickerDialog2() {

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
            if (monthOfYear < 10){
                m2 = "0"+monthOfYear;
            }else{
                m2 = String.valueOf(monthOfYear);
            }
            if (dayOfMonth < 10){
                d2 = "0"+dayOfMonth;
            }else{
                d2 = String.valueOf(dayOfMonth);
            }
            edit_birthday.setText(year+"-"+m2+"-"+d2);
        }
    };


    private View.OnClickListener onSubmitClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new InsertAsyn().execute("https://pigaboo.xyz/Insert_ProfilePig.php");

        }
    };


    public class InsertAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("pig_id", edit_id.getText().toString())
                        .add("pig_recorddate", edit_opendate.getText().toString())
                        .add("pig_birthday", edit_birthday.getText().toString())
                        .add("pig_breed", edit_breed.getText().toString())
                        .add("pig_idbreeder", edit_dadId1.getText().toString())
                        .add("pig_idbreeder2", edit_momId.getText().toString())
                        .add("pig_from", edit_form.getText().toString())
                        .add("pig_idreserve", edit_reserveID.getText().toString())
                        .add("farm_id", farm_id)
                        .add("idrecordtype_pig", "1")
                        .add("unit_id", unit_id)
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
                edit_id.setText("");
                edit_birthday.setText("");
                edit_breed.setText("");
                edit_dadId1.setText("");
                edit_momId.setText("");
                edit_form.setText("");
                edit_reserveID.setText("");
            } else {
                Toast.makeText(getActivity(), "ไม่สามารถบันทึกข้อมูลได้", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
