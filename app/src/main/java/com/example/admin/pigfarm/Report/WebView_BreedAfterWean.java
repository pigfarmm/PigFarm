package com.example.admin.pigfarm.Report;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.pigfarm.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WebView_BreedAfterWean extends AppCompatActivity {

    String farm_id,last_day,ip_number,ip_type,showtype,start,end;
    private WebView webview;
    ProgressDialog pDialog;
    String pdffile,pig_id,unit_id,unit_name;
    StringBuffer buffer;
//    SwipeRefreshLayout mySwipeRefreshLayout;

    CardView download;
    PDFView p;
    TextView txt_about;
    ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pigdata_report);

        p = findViewById(R.id.pdfView);
        download = findViewById(R.id.download);
        txt_about = findViewById(R.id.txt_about);
        img_back = findViewById(R.id.img_back);

        txt_about.setText("รายงานวิเคราะห์ผสมหลังหย่านม");


        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        farm_id = farm.getString("farm_id", "");
        unit_id = farm.getString("unit_id", "");
        unit_name = farm.getString("unit_name", "");

        Intent intent = getIntent();
        pdffile = intent.getStringExtra("url");
        last_day = intent.getStringExtra("last_day");
        ip_number = intent.getStringExtra("ip_number");
        ip_type = intent.getStringExtra("ip_type");
        start = intent.getStringExtra("start");
        end = intent.getStringExtra("end");

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
        buffer.append(("&start=")+start);
        buffer.append(("&end=")+end);
        buffer.append(("&unit_id=")+unit_id);
        buffer.append(("&unit_name=")+unit_name);

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
                Intent intent = new Intent(WebView_BreedAfterWean.this, Report_BreedAfterWean.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private class ShowPDF extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                WebView_BreedAfterWean.this.runOnUiThread(new Runnable() {
                    public void run() {
                        pDialog = new ProgressDialog(WebView_BreedAfterWean.this);
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
