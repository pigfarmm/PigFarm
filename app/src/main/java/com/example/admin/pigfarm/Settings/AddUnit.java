package com.example.admin.pigfarm.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admin.pigfarm.R;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AddUnit extends AppCompatActivity {

    EditText edit_unit;
    Spinner spin_typeunit;
    Button btn_saveunit;
    String farm_id,typeunit_id;
    ImageView img_back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addunit);

        SharedPreferences shared = this.getSharedPreferences("Farm", Context.MODE_PRIVATE);
        farm_id = shared.getString("farm_id", "");

        edit_unit = findViewById(R.id.edit_unit);
        spin_typeunit = findViewById(R.id.spin_typeunit);
        btn_saveunit = findViewById(R.id.btn_saveunit);
        img_back = findViewById(R.id.img_back);

        final String[] eventStr = getResources().getStringArray(R.array.type_of_unit);
        ArrayAdapter<String> adapterEvent = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, eventStr);
        spin_typeunit.setAdapter(adapterEvent);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddUnit.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_saveunit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Addunit().execute("https://pigaboo.xyz/Addunit.php");
            }
        });


    }

    private class Addunit extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try{

                if (spin_typeunit.getSelectedItem().toString().equals("พ่อพันธุ์")){
                    typeunit_id = "1";
                }else if (spin_typeunit.getSelectedItem().toString().equals("แม่พันธุ์")){
                    typeunit_id = "2";
                }else if (spin_typeunit.getSelectedItem().toString().equals("โรงขาย")){
                    typeunit_id = "3";
                }else if (spin_typeunit.getSelectedItem().toString().equals("โรงอาหาร")){
                    typeunit_id = "4";
                }else if (spin_typeunit.getSelectedItem().toString().equals("สุกรขุน")){
                    typeunit_id = "5";
                }else if (spin_typeunit.getSelectedItem().toString().equals("สุกรทดแทน")){
                    typeunit_id = "6";
                }else if (spin_typeunit.getSelectedItem().toString().equals("อนุบาล")){
                    typeunit_id = "7";
                }

                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("farm_id", farm_id)
                        .add("unit_name", edit_unit.getText().toString())
                        .add("typeunit_id", typeunit_id)
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
                Toast.makeText(AddUnit.this, "เพิ่มยูนิตเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AddUnit.this, SettingsActivity.class);
                startActivity(i);
                finish();

            }else {
                Toast.makeText(AddUnit.this, "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }

    }
}
