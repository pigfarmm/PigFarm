package com.example.admin.pigfarm.Report;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.pigfarm.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WebView_CompareUnit extends AppCompatActivity {

    String farm_id,last_day,ip_number,ip_type,showtype,list;
    private WebView webview;
    ProgressDialog pDialog;
    String pdffile,pig_id,unit_id,unit_name,first,second,third,fourth,fiveth,sixth,seventh,eighth,nineth,tenth;
    StringBuffer buffer;
    ImageView img_back;


    CardView download;
    PDFView p;
    TextView txt_about;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pigdata_report);

        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        farm_id = farm.getString("farm_id", "");
        unit_id = farm.getString("unit_id", "");
        unit_name = farm.getString("unit_name", "");

        p = findViewById(R.id.pdfView);
        download = findViewById(R.id.download);
        txt_about = findViewById(R.id.txt_about);
        img_back = findViewById(R.id.img_back);

        txt_about.setText("รายงานเปรียบเทียบยูนิต");

        Intent intent = getIntent();
        pdffile = intent.getStringExtra("url");
        last_day = intent.getStringExtra("last_day");
        ip_number = intent.getStringExtra("ip_number");
        ip_type = intent.getStringExtra("ip_type");
        list = intent.getStringExtra("list");

        String[] arr = list.split(",");
        first = arr[0];
        second = arr[1];
        if (arr.length == 3){
            third = arr[2];
        }else if(arr.length == 4){
            fourth = arr[3];
        }else if(arr.length == 5){
            fiveth = arr[4];
        }else if(arr.length == 6){
            sixth = arr[5];
        }else if(arr.length == 7){
            seventh = arr[6];
        }else if(arr.length == 8){
            eighth = arr[7];
        }else if(arr.length == 9){
            nineth = arr[8];
        }else if(arr.length == 10){
            tenth = arr[9];
        }

//        Toast.makeText(WebView_CompareUnit.this, first+" + "+second+" + "+third+" + "+fourth, Toast.LENGTH_SHORT).show();
        Log.d("unit","is :"+first+" + "+second+" + "+third);

        if(ip_type.equals("วัน")){
            showtype = "DAY";
        }else if(ip_type.equals("เดือน")){
            showtype = "MONTH";
        }else if(ip_type.equals("ปี")){
            showtype = "YEAR";
        }else if(ip_type.equals("สัปดาห์")){
            showtype = "WEEK";
        }

        buffer = new StringBuffer(pdffile);
        buffer.append("?");
        buffer.append(("farm_id=")+farm_id);
        buffer.append(("&lastday=")+last_day);
        buffer.append(("&ip_number=")+ip_number);
        buffer.append(("&ip_type=")+showtype);
        buffer.append(("&unit_name=")+unit_name);
        buffer.append(("&first=")+first);
        buffer.append(("&second=")+second);
        if (third != null){
            buffer.append(("&third=")+third);
        }if (fourth != null){
            buffer.append(("&fourth=")+fourth);
        }if (fiveth != null){
            buffer.append(("&fiveth=")+fiveth);
        }if (sixth != null){
            buffer.append(("&sixth=")+sixth);
        }if (seventh != null){
            buffer.append(("&seventh=")+seventh);
        }if (eighth != null){
            buffer.append(("&eighth=")+eighth);
        }if (nineth != null){
            buffer.append(("&nineth=")+nineth);
        }if (tenth != null){
            buffer.append(("&tenth=")+tenth);
        }


        new ShowPDF().execute();
        Log.d("show url" ,buffer.toString());

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(buffer.toString())));
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WebView_CompareUnit.this, Report_Exclude.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private class ShowPDF extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                WebView_CompareUnit.this.runOnUiThread(new Runnable() {
                    public void run() {
                        pDialog = new ProgressDialog(WebView_CompareUnit.this);
                        pDialog.setTitle("กำลังออกรายงาน");
                        pDialog.setMessage("โปรดรอสักครู่...");
                        pDialog.setIndeterminate(false);
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }
                });

                String url = buffer.toString();
                InputStream input = new URL(url).openStream();
                p.fromStream(input).enableSwipe(true).load();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pDialog.dismiss();
        }

    }

}
