package com.example.admin.pigfarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.admin.pigfarm.BodyAnalyze.CameraAPI;
import com.example.admin.pigfarm.BodyAnalyze.CameraActivity;
import com.example.admin.pigfarm.BodyAnalyze.HomeBCS;
import com.example.admin.pigfarm.ManageData_Page.MainActivity_ManageData;
import com.example.admin.pigfarm.Report.PigData_Report;
import com.example.admin.pigfarm.Report.Report_BCS;
import com.example.admin.pigfarm.Report.Report_Home;
import com.example.admin.pigfarm.Report.Report_Main;
import com.example.admin.pigfarm.Settings.SettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class Home extends AppCompatActivity{

    TextView txt_user, txt_farm, txt_unit,txt_profile,txt_event,txt_data,txt_pigimgpro,txt_reportXX,txt_adduser,txt_report;
    CardView card_settings, card_profile, card_event, card_report, card_data,card_pigimgpro;
    String farm_name,farm_id,unit_name,username,password,owner,getfarm_id,LoginStatus,url;
    private SwipeRefreshLayout mSwipeRefresh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        SharedPreferences shared = getSharedPreferences("Login", Context.MODE_PRIVATE);
        username = shared.getString("username", "");
        password = shared.getString("pass", "");
        owner = shared.getString("owner", "");

        SharedPreferences farm = getSharedPreferences("Farm", Context.MODE_PRIVATE);
        farm_name = farm.getString("farm_name","");
        getfarm_id = farm.getString("farm_id","");
        unit_name = farm.getString("unit_name","");


        url = "https://pigaboo.xyz/login2.php?username="+username+"&password="+password;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                CheckStatus(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Home.this, "เชื่อมต่อ Server ไม่ได้ โปรดลองอีกครั้ง", Toast.LENGTH_SHORT).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(Home.this.getApplicationContext());
        requestQueue.add(stringRequest);


//        mSwipeRefresh = findViewById(R.id.swiperefresh);
//        mSwipeRefresh.setOnRefreshListener(this);

        txt_user = findViewById(R.id.txt_user);
        txt_farm = findViewById(R.id.txt_farm);
        txt_unit = findViewById(R.id.txt_unit);
        txt_profile = findViewById(R.id.txt_profile);
        txt_event = findViewById(R.id.txt_event);
        txt_data = findViewById(R.id.txt_data);
        txt_pigimgpro = findViewById(R.id.txt_pigimgpro);
        txt_reportXX = findViewById(R.id.txt_reportXX);
        txt_adduser = findViewById(R.id.txt_adduser);
        txt_report = findViewById(R.id.txt_report);

        card_profile = findViewById(R.id.card_profile);
        card_event = findViewById(R.id.card_event);
        card_data = findViewById(R.id.card_data);
        card_report = findViewById(R.id.card_report);
        card_settings = findViewById(R.id.card_settings);
        card_pigimgpro = findViewById(R.id.card_pigimgpro);

        txt_user.setText(owner);
        txt_farm.setText(farm_name);
        txt_unit.setText(unit_name);

        txt_user.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
        txt_farm.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
        txt_unit.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
        txt_profile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
        txt_event.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
        txt_data.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
        txt_pigimgpro.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
        txt_reportXX.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
        txt_adduser.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);
        txt_report.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25f);

        card_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Open_Profile.class);
                startActivity(intent);
            }
        });

        card_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Event_main.class);
                startActivity(intent);
            }
        });

        card_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, MainActivity_ManageData.class);
                startActivity(intent);
            }
        });

        card_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Report_Main.class);
                startActivity(intent);
            }
        });

        card_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        card_pigimgpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Report_BCS.class);  //CameraActivity
                startActivity(intent);
            }
        });

    }

    private void CheckStatus(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");

                for (int i = 0; i<result.length(); i++){
                    JSONObject collectData = result.getJSONObject(i);
                    LoginStatus = collectData.getString("LoginStatus");

                    if (LoginStatus.equals("1")){
                        android.app.AlertDialog.Builder builder2 = new android.app.AlertDialog.Builder(Home.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
                        builder2.setCancelable(false);
                        builder2.setMessage("ขณะนี้กำลังปรับปรุงระบบ ขออภัยในความไม่สะดวก");
                        builder2.setNegativeButton("ตกลง", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                                System.exit(0);
                            }
                        });

                        android.app.AlertDialog dialog = builder2.create();
                        dialog.show();


                    }
            }

        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


        public void onLogoutClick(View v) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
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
                            editor2.commit();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this, R.style.Theme_AppCompat_Light_Dialog_Alert);
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


//    @Override
//    public void onRefresh() {
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mSwipeRefresh.setRefreshing(false);
//            }
//        },2000);
//    }
}
