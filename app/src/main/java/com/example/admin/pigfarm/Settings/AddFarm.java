package com.example.admin.pigfarm.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.pigfarm.LoginActivity;
import com.example.admin.pigfarm.R;
import com.example.admin.pigfarm.Register;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class AddFarm extends AppCompatActivity {

    EditText edit_farm,edit_field,edit_addrnumber,edit_tumbon,edit_amphoe,edit_province,edit_code,edit_tel;
    Button btn_saveregis;
    ImageView img_back;
    String member_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfarm);

        SharedPreferences shared = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        member_id = shared.getString("member_id", "");

        edit_farm = findViewById(R.id.edit_farm);
        edit_field = findViewById(R.id.edit_field);
        edit_addrnumber = findViewById(R.id.edit_addrnumber);
        edit_tumbon = findViewById(R.id.edit_tumbon);
        edit_amphoe = findViewById(R.id.edit_amphoe);
        edit_province = findViewById(R.id.edit_province);
        edit_code = findViewById(R.id.edit_code);
        img_back = findViewById(R.id.img_back);
        edit_tel = findViewById(R.id.edit_tel);
        btn_saveregis = findViewById(R.id.btn_saveregis);

        btn_saveregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Addfarm().execute("https://pigaboo.xyz/Addfarm.php");
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddFarm.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private class Addfarm extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("member_id", member_id)
                        .add("farm_name", edit_farm.getText().toString())
                        .add("farm_field", edit_field.getText().toString())
                        .add("farm_addrnumber", edit_addrnumber.getText().toString())
                        .add("farm_tumbon", edit_tumbon.getText().toString())
                        .add("farm_amphoe", edit_amphoe.getText().toString())
                        .add("farm_province", edit_province.getText().toString())
                        .add("farm_code", edit_code.getText().toString())
                        .add("farm_tel", edit_tel.getText().toString())
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
                Toast.makeText(AddFarm.this, "เพิ่มฟาร์มเรียบร้อยแล้ว",Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AddFarm.this, SettingsActivity.class);
                startActivity(i);
                finish();

            }else {
                Toast.makeText(AddFarm.this, "ไม่สามารถบันทึกข้อมูลได้",Toast.LENGTH_SHORT).show();
            }
        }
    }
}


