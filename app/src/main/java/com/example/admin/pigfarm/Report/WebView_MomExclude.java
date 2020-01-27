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

public class WebView_MomExclude extends AppCompatActivity {

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

        txt_about.setText("แม่พันธุ์ที่คัดทั้ง");

        Intent intent = getIntent();
        pdffile = intent.getStringExtra("url");
        start_date = intent.getStringExtra("start_date");
        end_date = intent.getStringExtra("end_date");
        condition_one = intent.getStringExtra("condition_one");
        condition_two = intent.getStringExtra("condition_two");
        condition_three = intent.getStringExtra("condition_three");

        if (condition_one.equals("สาเหตุที่คัดทิ้ง")){
            condition_one = "cause_exclude";
        }else if (condition_one.equals("จุดที่ถูกคัดทิ้ง")){
            condition_one = "place_exclude";
        }else if (condition_one.equals("ลักษณะการคัดทิ้ง")){
            condition_one = "style_exclude";
        }else if (condition_one.equals("ลำดับท้อง")){
            condition_one = "preglist_exclude";
        }else if (condition_one.equals("ไม่มีเงื่อนไข")){
            condition_one = "nocon_exclude";
        }

        if (condition_two.equals("มากกว่า")){
            condition_two = "more";
        }else if (condition_two.equals("น้อยกว่า")){
            condition_two = "less";
        }else if (condition_two.equals("เท่ากับ")){
            condition_two = "equal";
        }

        if (condition_three.equals("เข้าฝูง-ผสม")){
            condition_three = "newpig_breed";
        }else if (condition_three.equals("หย่านม-ผสม")){
            condition_three = "wean_breed";
        }else if (condition_three.equals("ผสม-คลอด")){
            condition_three = "breed_givebirth";
        }else if (condition_three.equals("คลอด-หย่านม")){
            condition_three = "givebirth_wean";
        }else if (condition_three.equals("ขาย")){
            condition_three = "sell";
        }else if (condition_three.equals("ตาย")){
            condition_three = "died";
        }else if (condition_three.equals("ทำลาย")){
            condition_three = "destroy";
        }else if (condition_three.equals("ย้าย")){
            condition_three = "move";
        }else if (condition_three.equals("ไม่ทราบ")){
            condition_three = "unknow";
        }else if (condition_three.equals("ไม่เป็นสัด")){
            condition_three = "01";
        }else if (condition_three.equals("ผสมไม่ติด")){
            condition_three = "02";
        }else if (condition_three.equals("ตรวจพบไม่ท้อง")){
            condition_three = "03";
        }else if (condition_three.equals("ท้องลม")){
            condition_three = "04";
        }else if (condition_three.equals("แท้ง")){
            condition_three = "05";
        }else if (condition_three.equals("พ่อไม่กำหนัด")){
            condition_three = "06";
        }else if (condition_three.equals("น้ำเชื้อไม่ดี")){
            condition_three = "07";
        }else if (condition_three.equals("คลอดยาก")){
            condition_three = "08";
        }else if (condition_three.equals("ช่องคลอดทะลัก")){
            condition_three = "09";
        }else if (condition_three.equals("มดลูกทะลัก")){
            condition_three = "10";
        }else if (condition_three.equals("ทวารทะลัก")){
            condition_three = "11";
        }else if (condition_three.equals("มดลูกเป็นหนอง")){
            condition_three = "12";
        }else if (condition_three.equals("ให้ลูกตาย/มัมมี่")){
            condition_three = "13";
        }else if (condition_three.equals("ให้ลูกพิการ")){
            condition_three = "14";
        }else if (condition_three.equals("ให้ลูกตัวเล็ก")){
            condition_three = "15";
        }else if (condition_three.equals("ขนาดครอกเล็ก")){
            condition_three = "16";
        }else if (condition_three.equals("เลี้ยงลูกไม่ดี")){
            condition_three = "17";
        }else if (condition_three.equals("ครอกป่วยเป็นโรค")){
            condition_three = "18";
        }else if (condition_three.equals("เต้านมอักเสบ")){
            condition_three = "19";
        }else if (condition_three.equals("หนองไหล")){
            condition_three = "20";
        }else if (condition_three.equals("ไม่มีน้ำนม")){
            condition_three = "21";
        }else if (condition_three.equals("ปัญหาเต้านม")){
            condition_three = "22";
        }else if (condition_three.equals("ลักษณะไม่ดี")){
            condition_three = "23";
        }else if (condition_three.equals("เดินขากระเผลก")){
            condition_three = "24";
        }else if (condition_three.equals("เจ็บส่วนขา")){
            condition_three = "25";
        }else if (condition_three.equals("เจ็บส่วนกีบ")){
            condition_three = "26";
        }else if (condition_three.equals("ล้มไม่ลุก")){
            condition_three = "27";
        }else if (condition_three.equals("บาดเจ็บอื่นๆ")){
            condition_three = "28";
        }else if (condition_three.equals("เป็นฝี")){
            condition_three = "29";
        }else if (condition_three.equals("เป็นแผลหลุม")){
            condition_three = "30";
        }else if (condition_three.equals("ตัวผอมบาง")){
            condition_three = "31";
        }else if (condition_three.equals("ตัวใหญ่เกิน")){
            condition_three = "32";
        }else if (condition_three.equals("อายุมาก")){
            condition_three = "33";
        }else if (condition_three.equals("ขี้เรื้อน")){
            condition_three = "34";
        }else if (condition_three.equals("โรคทางเดินปัสสาวะ")){
            condition_three = "35";
        }else if (condition_three.equals("โรคระบบไหลเวียนโลหิต")){
            condition_three = "36";
        }else if (condition_three.equals("โรคทางเดินอาหาร")){
            condition_three = "37";
        }else if (condition_three.equals("โรคผิวหนัง")){
            condition_three = "38";
        }else if (condition_three.equals("โรคระบบประสาท")){
            condition_three = "39";
        }else if (condition_three.equals("โรคระบบหายใจ")){
            condition_three = "40";
        }else if (condition_three.equals("โรคระบบกล้ามเนื้อ")){
            condition_three = "41";
        }else if (condition_three.equals("โรคอื่นๆ")){
            condition_three = "42";
        }else if (condition_three.equals("ตายกระทันหัน")){
            condition_three = "43";
        }else if (condition_three.equals("พฤติกรรมดุร้าย")){
            condition_three = "44";
        }else if (condition_three.equals("เพื่อการจัดการ")){
            condition_three = "45";
        }else if (condition_three.equals("ล้างฝูง")){
            condition_three = "46";
        }else if (condition_three.equals("ทดสอบ&คัดทิ้ง")){
            condition_three = "47";
        }else if (condition_three.equals("โตช้า")){
            condition_three = "48";
        }else if (condition_three.equals("คุณภาพซากไม่ดี")){
            condition_three = "49";
        }else if (condition_three.equals("ปัญหาเลือดชิด")){
            condition_three = "50";
        }else if (condition_three.equals("ไม่ทราบสาเหตุ")){
            condition_three = "51";
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
                Intent intent = new Intent(WebView_MomExclude.this, Report_Exclude.class);
                startActivity(intent);
                finish();
            }
        });


    }

    private class ShowPDF extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                WebView_MomExclude.this.runOnUiThread(new Runnable() {
                    public void run() {
                        pDialog = new ProgressDialog(WebView_MomExclude.this);
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
