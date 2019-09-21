package com.example.admin.pigfarm.Report;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.admin.pigfarm.R;

import java.net.URLEncoder;

public class WebView_GroupBredder extends AppCompatActivity {

    String farm_id,last_day,ip_number,ip_type , showtype,start,end;
    private WebView webview;
    ProgressDialog pDialog;
    String pdffile,pig_id;
    StringBuffer buffer;
//    SwipeRefreshLayout mySwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groupbredder_report);

        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        farm_id = farm.getString("farm_id", "");

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

        buffer = new StringBuffer("https://drive.google.com/viewerng/viewer?url=");
        buffer.append(URLEncoder.encode(pdffile)+"?");
        buffer.append(URLEncoder.encode("farm_id=")+farm_id);
        buffer.append(URLEncoder.encode("&last_day=")+last_day);
        buffer.append(URLEncoder.encode("&ip_number=")+ip_number);
        buffer.append(URLEncoder.encode("&ip_type=")+showtype);
        buffer.append(URLEncoder.encode("&start=")+start);
        buffer.append(URLEncoder.encode("&end=")+end);

        Log.d("show url" ,buffer.toString());

        webview = findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new MyWebViewClient());
        webview.loadUrl(buffer.toString());


    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            pDialog = new ProgressDialog(WebView_GroupBredder.this);
            pDialog.setTitle("กำลังออกรายงาน");
            pDialog.setMessage("โปรดรอสักครู่...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (pDialog!=null){
                pDialog.dismiss();
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            Log.d("webview",""+error);


        }
    }
}
