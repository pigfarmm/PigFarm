package com.example.admin.pigfarm;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity {
    private Handler handler = new Handler();
    String username,password,url,farm_id,user,pass,member_id,url2,farm_name,farm_field,farm_addrnumber,farm_tumbon,farm_amphoe,farm_province,farm_code,farm_tel,owner,unit_id;
    Button btnSubmit;
    private ProgressDialog progressbar;
    TextView txtUsername,txtPassword,linkregis;
    private String[] convert_to_string,convert_farmid;
    AlertDialog.Builder alt_bld;
    String unit_name,LoginStatus;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnSubmit =findViewById(R.id.btnSubmit);


//        linkregis = findViewById(R.id.linkregis);

//        linkregis.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent2 = new Intent(LoginActivity.this, Register.class);
//                startActivity(intent2);
//            }
//        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressbar = new ProgressDialog(LoginActivity.this);
                progressbar.setMessage("กรุณารอสักครู่...");
                progressbar.setCancelable(false);
                progressbar.setIndeterminate(true);


               username = txtUsername.getText().toString();
               password = txtPassword.getText().toString();

                if (username.isEmpty() || password.isEmpty() ) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                    builder.setCancelable(true);
                    builder.setMessage("กรุณากรอกข้อมูลให้ครบถ้วน");
                    builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();


                } else {
                    progressbar.show();

                    url = "https://pigaboo.xyz/login2.php?username="+username+"&password="+password;
                    StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Login(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "เชื่อมต่อ Server ไม่ได้ โปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
                        }
                    }
                    );
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this.getApplicationContext());
                    requestQueue.add(stringRequest);

                }
            }
        });
    }


    public void Login(String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

            if (result.length() == 0) {
                progressbar.dismiss();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                builder.setCancelable(false);
                builder.setMessage("ชื่อผู้ใช้ / รหัสผ่านไม่ถูกต้อง");
                builder.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                android.app.AlertDialog dialog = builder.create();
                dialog.show();

            }else{
                for (int i = 0; i<result.length(); i++){
                    JSONObject collectData = result.getJSONObject(i);
                    user = collectData.getString("username");
                    pass = collectData.getString("password");
                    member_id = collectData.getString("member_id");
                    owner = collectData.getString("owner");
                    farm_id = collectData.getString("farm_id");
                    farm_name = collectData.getString("farm_name");
                    LoginStatus = collectData.getString("LoginStatus");
//                String unit_name = collectData.getString("unit_name");

                    if (LoginStatus.equals("1")){
                        progressbar.dismiss();
                        android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(LoginActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                        builder2.setCancelable(false);
                        builder2.setMessage("ขณะนี้กำลังปรับปรุงระบบ ขออภัยในความไม่สะดวก");
                        builder2.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        android.app.AlertDialog dialog = builder2.create();
                        dialog.show();


                    }else{
                        SharedPreferences shared = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();

                        editor.putString("username",user);
                        editor.putString("pass",pass);
                        editor.putString("member_id",member_id);
                        editor.putString("owner",owner);

                        editor.commit();

                        QueryUnit();
                    }







                }

            }



        }catch (JSONException ex) {
            ex.printStackTrace();
        }

    }

    private void QueryUnit() {
        url2 = "https://pigaboo.xyz/select_unit.php?farm_id="+farm_id;
        StringRequest stringRequest2 = new StringRequest(url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showRadioButtonDialog(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "เชื่อมต่อ Server ไม่ได้ โปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue2 = Volley.newRequestQueue(LoginActivity.this.getApplicationContext());
        requestQueue2.add(stringRequest2);
    }

    private void showRadioButtonDialog(String response) {
        progressbar.dismiss();
        try {
            JSONObject jsonObject2 = new JSONObject(response);
            JSONArray result2 = jsonObject2.getJSONArray("result");

            final List<String> stringList = new ArrayList<>();
            final List<String> stringIDFarm = new ArrayList<>();

            for (int i = 0; i<result2.length(); i++){
                JSONObject collectData2 = result2.getJSONObject(i);
                unit_id = collectData2.getString("unit_id");
                unit_name = collectData2.getString("unit_name");


                alt_bld = new AlertDialog.Builder(this);
                alt_bld.setTitle("โปรดเลือกยูนิต");

                stringList.add(unit_name);
                stringIDFarm.add(unit_id);

                convert_to_string = new String[stringList.size()];
                convert_to_string = stringList.toArray(convert_to_string);
                convert_farmid = new String[stringIDFarm.size()];
                convert_farmid = stringIDFarm.toArray(convert_farmid);

                alt_bld.setSingleChoiceItems(stringList.toArray(new String[stringList.size()]), -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                            SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = farm.edit();
                        editor2.putString("farm_id",farm_id);
                        editor2.putString("farm_name",farm_name);
                        editor2.putString("unit_id",convert_farmid[item]);
                        editor2.putString("unit_name", convert_to_string[item]);

//                        editor2.putString("farm_field",farm_field);
//                        editor2.putString("farm_addrnumber",farm_addrnumber);
//                        editor2.putString("farm_tumbon",farm_tumbon);
//                        editor2.putString("farm_amphoe",farm_amphoe);
//                        editor2.putString("farm_province",farm_province);
//                        editor2.putString("farm_code",farm_code);
//                        editor2.putString("farm_tel",farm_tel);
                editor2.apply();
                editor2.commit();

                Intent intent = new Intent(LoginActivity.this, Home.class);
                intent.putExtra("LoginStatus",LoginStatus);
                startActivity(intent);
                finish();


                    }
                });

                alt_bld.setPositiveButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Intent intent = new Intent(LoginActivity.this, Home.class);
//                        startActivity(intent);
//                        finish();

                    }
                });

            }

            AlertDialog alert = alt_bld.create();
            alert.show();


            }

            catch (JSONException ex) {
            ex.printStackTrace();
        }


    }

}

