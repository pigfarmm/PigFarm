package com.example.admin.pigfarm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.R;
import com.example.admin.pigfarm.ManageData_Page.MainActivity_ManageData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;



public class Home extends Activity{

    ImageButton img_logout;
    TextView txt_user, txt_farm, txt_unit;
    CardView card_setting, card_profile, card_event, card_report, card_data;
    String farm_owner ="";
    String farm_name = "";
    String farm_id = "";
    String unit_name = "";
    String username = "";
    String password = "";
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        SharedPreferences shared = getSharedPreferences("Login", Context.MODE_PRIVATE);
        username = shared.getString("username", "");
        password = shared.getString("pass", "");

        mSwipeRefresh = findViewById(R.id.swipe_refresh_layout);

        txt_user = findViewById(R.id.txt_user);
        txt_farm = findViewById(R.id.txt_farm);
        txt_unit = findViewById(R.id.txt_unit);

        String url = "http://pigaboo.xyz/Query_Farm.php?username="+username+"&password="+password;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ไม่สามารถดึงข้อมูลได้ โปรดตรวจสอบการเชื่อมต่อ", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        requestQueue.add(stringRequest);

    }

    public void showJSON(String response){
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray result = jsonObject.getJSONArray("result");


                for (int i = 0; i<result.length(); i++){
                    JSONObject collectData = result.getJSONObject(i);
                    farm_owner = collectData.getString("farm_owner");
                    farm_name = collectData.getString("farm_name");
                    unit_name = collectData.getString("unit_name");
                    farm_id = collectData.getString("farm_id");
                    txt_user.setText(farm_owner);
                    txt_farm.setText(farm_name);
                    txt_unit.setText(unit_name);

                    card_profile = findViewById(R.id.card_profile);
                    card_event = findViewById(R.id.card_event);
                    card_data = findViewById(R.id.card_data);



                    card_profile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Home.this, Open_Profile.class);
                            SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = farm.edit();
                            editor2.putString("farm_name",farm_name);
                            editor2.putString("unit_name",unit_name);
                            editor2.putString("farm_id",farm_id);
                            editor2.commit();
                            startActivity(intent);
                        }
                    });

                    card_event.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Home.this, Event_main.class);
                            SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = farm.edit();
                            editor2.putString("farm_name",farm_name);
                            editor2.putString("unit_name",unit_name);
                            editor2.putString("farm_id",farm_id);
                            editor2.commit();
                            startActivity(intent);
                        }
                    });

                    card_data.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Home.this, MainActivity_ManageData.class);
                            SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = farm.edit();
                            editor2.putString("farm_name",farm_name);
                            editor2.putString("unit_name",unit_name);
                            editor2.putString("farm_id",farm_id);
                            editor2.commit();
                            startActivity(intent);
                        }
                    });

                }
            }catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        public void onLogoutClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
            builder.setCancelable(true);
            builder.setMessage("คุณต้องการออกระบบใช่หรือไม่");
            builder.setPositiveButton("ใช่",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences shared = getSharedPreferences("Login", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = shared.edit();
                            editor.clear();
                            editor.commit();

                            SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2 = farm.edit();
                            editor2.clear();
                            editor2.apply();

                            Intent intent = new Intent(Home.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
            builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setCancelable(true);
        builder.setMessage("คุณต้องการออกระบบใช่หรือไม่");
        builder.setPositiveButton("ใช่",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences shared = getSharedPreferences("Login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shared.edit();
                        editor.clear();
                        editor.commit();

                        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor2 = farm.edit();
                        editor2.clear();
                        editor2.apply();

                        Intent intent = new Intent(Home.this, LoginActivity.class);
                        startActivity(intent);
                    }
                });
        builder.setNegativeButton("ไม่ใช่", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }




}
