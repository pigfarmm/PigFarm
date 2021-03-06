package com.example.admin.pigfarm.Report;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.pigfarm.R;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.text.MessageFormat;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class WebView_Report_Status extends AppCompatActivity {

    String farm_id,unit_id,unit_name;
    private WebView webview;
    ProgressDialog pDialog;
    String pdffile,sort_by;
//    SwipeRefreshLayout mySwipeRefreshLayout;
    CardView download;
    PDFView p;
    StringBuffer buffer;
    TextView txt_about;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pigdata_report);

        p = findViewById(R.id.pdfView);
        download = findViewById(R.id.download);
        txt_about = findViewById(R.id.txt_about);

        txt_about.setText("รายงานสถานภาพพ่อแม่พันธุ์");

        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        farm_id = farm.getString("farm_id", "");
        unit_id = farm.getString("unit_id", "");
        unit_name = farm.getString("unit_name", "");

        Intent intent = getIntent();
        pdffile = intent.getStringExtra("url");


        buffer=new StringBuffer(pdffile);
        buffer.append("?");
        buffer.append(("farm_id=")+farm_id);
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


    }

    private class ShowPDF extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                WebView_Report_Status.this.runOnUiThread(new Runnable() {
                    public void run() {
                        pDialog = new ProgressDialog(WebView_Report_Status.this);
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

