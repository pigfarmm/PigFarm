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

public class WebView_MomBreed extends AppCompatActivity {

    String farm_id,start_date,end_date,condition_one,condition_two,condition_three;
    private WebView webview;
    ProgressDialog pDialog;
    String pdffile,pig_id,unit_id,unit_name;
    StringBuffer buffer;
    ImageView img_back;
    CardView download;
    PDFView p;
    TextView txt_about;

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

        txt_about.setText("แม่พันธุ์ที่ผสม");

        Intent intent = getIntent();
        pdffile = intent.getStringExtra("url");
        start_date = intent.getStringExtra("start_date");
        end_date = intent.getStringExtra("end_date");
        condition_one = intent.getStringExtra("condition_one");
        condition_two = intent.getStringExtra("condition_two");
        condition_three = intent.getStringExtra("condition_three");

        if (condition_one.equals("ครั้งที่ผสม")){
            condition_one = "count_breed";
        }else if(condition_one.equals("พ่อพันธุ์")){
            condition_one = "breeder";
        }else if (condition_one.equals("ลำดับท้อง")){
            condition_one = "preg_list";
        }else if (condition_one.equals("หย่านม-ผสม")){
            condition_one = "ev6_1";
        }else if (condition_one.equals("ไม่มีเงื่อนไข")){
            condition_one = "no_con";
        }

        if (condition_two.equals("มากกว่า")){
            condition_two = "more";
        }else if (condition_two.equals("น้อยกว่า")){
            condition_two = "less";
        }else if (condition_two.equals("เท่ากับ")){
            condition_two = "equal";
        }

        buffer = new StringBuffer(pdffile);
        buffer.append("?");
        buffer.append(("farm_id=")+farm_id);
        buffer.append(("&start_date=")+start_date);
        buffer.append(("&end_date=")+end_date);
        buffer.append(("&condition_one=")+condition_one);
        buffer.append(("&condition_two=")+condition_two);
        buffer.append(("&condition_three=")+condition_three);
        buffer.append(("&unit_id=")+unit_id);

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
                Intent intent = new Intent(WebView_MomBreed.this, Report_Exclude.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private class ShowPDF extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                WebView_MomBreed.this.runOnUiThread(new Runnable() {
                    public void run() {
                        pDialog = new ProgressDialog(WebView_MomBreed.this);
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
